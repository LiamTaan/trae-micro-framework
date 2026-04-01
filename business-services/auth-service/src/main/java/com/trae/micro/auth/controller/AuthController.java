package com.trae.micro.auth.controller;

import com.trae.micro.auth.dto.LoginRequest;
import com.trae.micro.auth.dto.LoginResponse;
import com.trae.micro.auth.feign.SystemFeignClient;
import com.trae.micro.auth.model.User;
import com.trae.micro.core.service.RedisService;
import com.trae.micro.auth.service.UserService;
import com.trae.micro.core.exception.BusinessException;
import com.trae.micro.core.model.Menu;
import com.trae.micro.core.model.R;
import com.trae.micro.core.security.JwtUtils;
import com.trae.micro.core.security.SecurityContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
    private final RedisService redisService;

    @Value("${jwt.expire:3600000}")
    private Long expire;

    public AuthController(UserService userService, JwtUtils jwtUtils, SystemFeignClient systemFeignClient, RedisService redisService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.systemFeignClient = systemFeignClient;
        this.redisService = redisService;
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

        // 将用户添加到在线用户列表
        redisService.addOnlineUser(user.getId(), accessToken, expire);

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
        // 获取当前用户信息
        SecurityContextHolder.UserDetails userDetails = SecurityContextHolder.getCurrentUser();
        if (userDetails != null) {
            // 从Redis中移除在线用户
            redisService.removeOnlineUser(userDetails.getUserId());
        }
        // 清除线程上下文
        SecurityContextHolder.clearCurrentUser();
        return R.success(null, "登出成功");
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/userInfo")
    @Operation(summary = "获取当前用户信息", description = "获取当前用户信息接口")
    public R<User> getUserInfo() {
        SecurityContextHolder.UserDetails userDetails = SecurityContextHolder.getCurrentUser();
        if (userDetails == null) {
            throw new BusinessException("用户未登录");
        }
        Long userId = userDetails.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return R.success(user, "获取用户信息成功");
    }
    
    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "刷新令牌接口")
    public R<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        // 验证刷新令牌
        Map<String, Object> claims = jwtUtils.parseToken(request.getRefreshToken());
        if (claims == null) {
            throw new BusinessException("刷新令牌无效");
        }
        
        Long userId = Long.parseLong(claims.get("userId").toString());
        String username = claims.get("username").toString();
        Long tenantId = Long.parseLong(claims.get("tenantId").toString());
        
        // 生成新的访问令牌
        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put("userId", userId);
        newClaims.put("username", username);
        newClaims.put("tenantId", tenantId);
        
        String accessToken = jwtUtils.generateToken(newClaims);
        String refreshToken = jwtUtils.generateToken(newClaims);
        
        // 构建响应
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpire(expire);
        
        return R.success(loginResponse, "令牌刷新成功");
    }
    
    /**
     * 刷新令牌请求
     */
    public static class RefreshTokenRequest {
        private String refreshToken;
        
        public String getRefreshToken() {
            return refreshToken;
        }
        
        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/changePassword")
    @Operation(summary = "修改密码", description = "修改密码接口")
    public R<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        SecurityContextHolder.UserDetails userDetails = SecurityContextHolder.getCurrentUser();
        if (userDetails == null) {
            throw new BusinessException("用户未登录");
        }
        Long userId = userDetails.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 验证旧密码
        if (!userService.verifyPassword(user, request.getOldPassword())) {
            throw new BusinessException("旧密码错误");
        }
        
        // 更新密码
        userService.updatePassword(userId, request.getNewPassword());
        
        return R.success(null, "密码修改成功");
    }
    
    /**
     * 修改密码请求
     */
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
        private String confirmPassword;
        
        public String getOldPassword() {
            return oldPassword;
        }
        
        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
        
        public String getConfirmPassword() {
            return confirmPassword;
        }
        
        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }
}