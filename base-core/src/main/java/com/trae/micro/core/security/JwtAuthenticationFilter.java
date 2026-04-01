package com.trae.micro.core.security;

import com.trae.micro.core.exception.BusinessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final ServiceTokenManager serviceTokenManager;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, ServiceTokenManager serviceTokenManager) {
        this.jwtUtils = jwtUtils;
        this.serviceTokenManager = serviceTokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 检查是否为服务间调用，验证服务间Token
        String serviceToken = request.getHeader("X-Service-Token");
        if (StringUtils.hasText(serviceToken)) {
            if (serviceTokenManager.validateServiceToken(serviceToken)) {
                // 服务Token有效，直接放行
                String serviceName = serviceTokenManager.getServiceNameFromToken(serviceToken);
                log.debug("服务间调用验证通过: 服务名称={}", serviceName);
                filterChain.doFilter(request, response);
                return;
            } else {
                // 服务Token无效
                log.error("服务间Token无效: {}", serviceToken);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":false,\"code\":401,\"message\":\"服务认证失败\"}");
                return;
            }
        }
        
        // 2. 普通用户请求，验证JWT
        try {
            // 获取token
            String token = getTokenFromRequest(request);

            // 如果请求头中有token且有效，则验证token
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                // 从token中获取用户信息
                Long userId = jwtUtils.getUserIdFromToken(token);
                String username = jwtUtils.getUsernameFromToken(token);

                // 设置用户信息到上下文
                com.trae.micro.core.security.SecurityContextHolder.UserDetails userDetails = new com.trae.micro.core.security.SecurityContextHolder.UserDetails(userId, username);
                com.trae.micro.core.security.SecurityContextHolder.setCurrentUser(userDetails);
                
                // 获取并设置租户ID
                Long tenantId = null;
                // 先从请求头中获取租户ID
                String tenantIdHeader = request.getHeader("X-Tenant-Id");
                if (StringUtils.hasText(tenantIdHeader)) {
                    try {
                        tenantId = Long.parseLong(tenantIdHeader);
                    } catch (NumberFormatException e) {
                        log.warn("请求头中X-Tenant-Id格式错误: {}", tenantIdHeader);
                    }
                }
                // 如果请求头中没有租户ID，尝试从JWT中获取
                if (tenantId == null) {
                    try {
                        tenantId = jwtUtils.parseToken(token).get("tenantId", Long.class);
                    } catch (Exception e) {
                        log.debug("从JWT中获取租户ID失败: {}", e.getMessage());
                    }
                }
                // 设置租户ID到上下文
                if (tenantId != null) {
                    TenantContextHolder.setTenantId(tenantId);
                    log.debug("当前租户ID: {}", tenantId);
                }
            }
        } catch (Exception e) {
            log.error("JWT认证失败: {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false,\"code\":401,\"message\":\"认证失败\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中获取token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}