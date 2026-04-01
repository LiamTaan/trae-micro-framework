package com.trae.micro.auth.controller;

import com.trae.micro.auth.dto.LoginRequest;
import com.trae.micro.auth.dto.LoginResponse;
import com.trae.micro.auth.feign.SystemFeignClient;
import com.trae.micro.auth.model.User;
import com.trae.micro.auth.service.UserService;
import com.trae.micro.core.exception.BusinessException;
import com.trae.micro.core.model.Menu;
import com.trae.micro.core.model.R;
import com.trae.micro.core.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证管理", description = "认证相关接口")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final SystemFeignClient systemFeignClient;

    @Value("${jwt.expire:3600000}")
    private Long expire;

    public AuthController(UserService userService, JwtUtils jwtUtils, SystemFeignClient systemFeignClient) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.systemFeignClient = systemFeignClient;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 查询用户
        User user = userService.getByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 验证密码
        if (!userService.verifyPassword(user, loginRequest.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 验证用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用，请联系管理员");
        }

        // 更新登录时间
        userService.updateLastLoginTime(user.getId(),user.getTenantId());

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("tenantId", user.getTenantId() != null ? user.getTenantId() : 0L);
        String accessToken = jwtUtils.generateToken(claims);
        String refreshToken = jwtUtils.generateToken(claims);

        // 构建响应
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpire(expire);
        loginResponse.setUser(user);
        
        // 获取用户菜单
        R<List<Menu>> menuResponse = systemFeignClient.getMenuListByUserId(user.getId());
        if (menuResponse.getSuccess()) {
            loginResponse.setMenus(menuResponse.getData());
        } else {
            log.warn("获取用户菜单失败: {}", menuResponse.getMessage());
        }

        return R.success(loginResponse, "登录成功");
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出接口")
    public R<Void> logout() {
        // 这里可以实现登出逻辑，如清除Redis中的token等
        return R.success(null, "登出成功");
    }
}