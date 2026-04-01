package com.trae.micro.core.config;

import com.trae.micro.core.security.JwtAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 基础安全配置
 * 所有微服务的SecurityConfig可以继承此类，减少重复代码
 * 注意：此类不是@Configuration，只是普通抽象类，用于被子类继承
 */
public abstract class BaseSecurityConfig {


    protected final JwtAuthenticationFilter jwtAuthenticationFilter;

    protected BaseSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * 构建基础安全配置
     * 子类在自己的@Bean方法中调用此方法
     */
    protected HttpSecurity buildBaseSecurityConfig(HttpSecurity http) throws Exception {
        return http
            // CSRF防护配置 - 前后端分离JWT认证无需CSRF保护
            .csrf(AbstractHttpConfigurer::disable)
            // 关闭X-Frame-Options
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            // 不创建会话
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置请求授权
            .authorizeHttpRequests(authz -> {
                // 默认允许Swagger相关路径
                authz.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
                // 子类可以重写此方法添加更多配置
                configureAuthorizeRequests(authz);
            })
            // 添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 配置请求授权规则
     * 子类可以重写此方法添加自定义规则
     */
    protected void configureAuthorizeRequests(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
        // 默认配置：所有请求需要认证
        authz.anyRequest().authenticated();
    }
}