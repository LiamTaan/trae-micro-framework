package com.trae.micro.auth.security;

import com.trae.micro.core.config.BaseSecurityConfig;
import com.trae.micro.core.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends BaseSecurityConfig {

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        super(jwtAuthenticationFilter);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 调用父类方法构建基础配置
        HttpSecurity configuredHttp = buildBaseSecurityConfig(http);
        // 构建并返回SecurityFilterChain
        return configuredHttp.build();
    }

    /**
     * 配置请求授权规则
     */
    @Override
    protected void configureAuthorizeRequests(
           AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
        // 允许所有用户访问的接口
        authz.requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/users/**").permitAll();
        // 其他接口需要认证
        authz.anyRequest().authenticated();
    }
}