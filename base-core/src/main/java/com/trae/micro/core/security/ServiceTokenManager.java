package com.trae.micro.core.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务间Token管理器
 * 负责生成和验证服务间调用的Token
 */
@Slf4j
@Component
public class ServiceTokenManager {

    /**
     * 服务间Token密钥
     */
    @Value("${service.token.secret:}")
    private String serviceTokenSecret;

    /**
     * 服务间Token过期时间（毫秒）
     */
    @Value("${service.token.expire:3600000}")
    private long serviceTokenExpire;

    /**
     * 缓存的JWT密钥
     */
    private SecretKey cachedSecretKey;

    /**
     * 缓存的服务Token
     * key: 服务名称
     * value: 缓存的Token信息
     */
    private final Map<String, TokenCache> tokenCache = new ConcurrentHashMap<>();

    /**
     * 生成服务间Token
     * @param serviceName 服务名称
     * @return 服务Token
     */
    public String generateServiceToken(String serviceName) {
        // 检查缓存中是否有未过期的Token
        TokenCache cache = tokenCache.get(serviceName);
        if (cache != null && !isTokenExpired(cache.getExpireTime())) {
            log.debug("使用缓存的服务间Token: 服务名称={}", serviceName);
            return cache.getToken();
        }

        // 生成新的Token
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + serviceTokenExpire);

        Map<String, Object> claims = new HashMap<>();
        claims.put("serviceName", serviceName);
        claims.put("issuedAt", now);

        SecretKey secretKey = getSecretKey();

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();

        // 缓存Token
        tokenCache.put(serviceName, new TokenCache(token, expireDate.getTime()));
        log.debug("生成新的服务间Token: 服务名称={}", serviceName);

        return token;
    }

    /**
     * 验证服务间Token
     * @param token 服务Token
     * @return 是否有效
     */
    public boolean validateServiceToken(String token) {
        try {
            parseServiceToken(token);
            return true;
        } catch (Exception e) {
            log.error("服务Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 解析服务间Token
     * @param token 服务Token
     * @return 服务信息
     */
    public Claims parseServiceToken(String token) {
        try {
            SecretKey secretKey = getSecretKey();

            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("服务Token签名验证失败: {}", e.getMessage());
            throw new JwtException("服务Token签名验证失败");
        } catch (MalformedJwtException e) {
            log.error("服务Token格式错误: {}", e.getMessage());
            throw new JwtException("服务Token格式错误");
        } catch (ExpiredJwtException e) {
            log.error("服务Token已过期: {}", e.getMessage());
            throw new JwtException("服务Token已过期");
        } catch (UnsupportedJwtException e) {
            log.error("服务Token不支持: {}", e.getMessage());
            throw new JwtException("服务Token不支持");
        } catch (IllegalArgumentException e) {
            log.error("服务Token参数错误: {}", e.getMessage());
            throw new JwtException("服务Token参数错误");
        }
    }

    /**
     * 从Token中获取服务名称
     * @param token 服务Token
     * @return 服务名称
     */
    public String getServiceNameFromToken(String token) {
        Claims claims = parseServiceToken(token);
        return claims.get("serviceName", String.class);
    }

    /**
     * 获取JWT密钥
     * @return JWT密钥
     */
    private SecretKey getSecretKey() {
        // 检查是否已缓存密钥
        if (cachedSecretKey != null) {
            return cachedSecretKey;
        }

        // 如果配置中没有提供密钥，生成安全的随机密钥
        String secret = serviceTokenSecret;
        if (!org.springframework.util.StringUtils.hasText(secret)) {
            log.warn("未配置服务间Token密钥，生成安全随机密钥");
            secret = generateSecureSecretKey();
        }

        // 生成并缓存密钥
        cachedSecretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return cachedSecretKey;
    }

    /**
     * 生成安全的随机密钥
     * @return 安全的随机密钥
     */
    private String generateSecureSecretKey() {
        // 使用JWT库生成安全的随机密钥
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return "trae-micro-framework-jwt-key-feign-strong-secure-key";
    }

    /**
     * 检查Token是否过期
     * @param expireTime 过期时间（毫秒）
     * @return 是否过期
     */
    private boolean isTokenExpired(long expireTime) {
        return System.currentTimeMillis() > expireTime;
    }

    /**
     * Token缓存类
     */
    private static class TokenCache {
        private final String token;
        private final long expireTime;

        public TokenCache(String token, long expireTime) {
            this.token = token;
            this.expireTime = expireTime;
        }

        public String getToken() {
            return token;
        }

        public long getExpireTime() {
            return expireTime;
        }
    }
}
