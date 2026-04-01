package com.trae.micro.core.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {
    
    /**
     * JWT 密钥
     */
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    /**
     * JWT 过期时间（毫秒）
     */
    @Value("${jwt.expire}")
    private long jwtExpire;

    /**
     * 生成JWT令牌
     * @param claims 自定义声明
     * @return JWT令牌
     */
    public String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + jwtExpire);
        
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token JWT令牌
     * @return 自定义声明
     */
    public Claims parseToken(String token) {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("JWT签名验证失败: {}", e.getMessage());
            throw new JwtException("签名验证失败");
        } catch (MalformedJwtException e) {
            log.error("JWT格式错误: {}", e.getMessage());
            throw new JwtException("令牌格式错误");
        } catch (ExpiredJwtException e) {
            log.error("JWT已过期: {}", e.getMessage());
            throw new JwtException("令牌已过期");
        } catch (UnsupportedJwtException e) {
            log.error("JWT不支持: {}", e.getMessage());
            throw new JwtException("令牌不支持");
        } catch (IllegalArgumentException e) {
            log.error("JWT参数错误: {}", e.getMessage());
            throw new JwtException("令牌参数错误");
        }
    }

    /**
     * 从JWT令牌中获取用户ID
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * 从JWT令牌中获取用户名
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username").toString();
    }

    /**
     * 验证JWT令牌
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}