package com.trae.micro.file.config;

import com.trae.micro.core.config.BaseSecurityConfig;
import com.trae.micro.core.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 文件服务安全配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends BaseSecurityConfig {

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        super(jwtAuthenticationFilter);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 调用父类方法构建基础配置
        HttpSecurity configuredHttp = buildBaseSecurityConfig(http);
        // 构建并返回SecurityFilterChain
        return configuredHttp.build();
    }

    // 文件服务无需额外配置，使用BaseSecurityConfig的默认规则
    /**
     * 配置请求授权规则
     */
    @Override
    protected void configureAuthorizeRequests(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
        // 允许所有用户访问的接口
        authz.requestMatchers("/api/v1/file/**").permitAll();
        // 其他接口需要认证
        authz.anyRequest().authenticated();
    }
}