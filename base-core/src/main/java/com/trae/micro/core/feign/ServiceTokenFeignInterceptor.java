package com.trae.micro.core.feign;

import com.trae.micro.core.security.ServiceTokenManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 服务间Token Feign拦截器
 * 自动为Feign请求添加服务间Token
 */
@Slf4j
@Component
public class ServiceTokenFeignInterceptor implements RequestInterceptor {

    private final ServiceTokenManager serviceTokenManager;

    /**
     * 当前服务名称
     */
    @Value("${spring.application.name}")
    private String currentServiceName;

    public ServiceTokenFeignInterceptor(ServiceTokenManager serviceTokenManager) {
        this.serviceTokenManager = serviceTokenManager;
    }

    @Override
    public void apply(RequestTemplate template) {
        // 生成服务间Token
        String serviceToken = serviceTokenManager.generateServiceToken(currentServiceName);
        // 添加到请求头
        template.header("X-Service-Token", serviceToken);
        log.debug("服务间调用添加Token: 服务名称={}, URL={}", currentServiceName, template.url());
    }
}
