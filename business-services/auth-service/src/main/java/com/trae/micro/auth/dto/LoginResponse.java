package com.trae.micro.auth.dto;

import com.trae.micro.auth.model.User;
import com.trae.micro.core.model.Menu;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 过期时间（单位：毫秒）
     */
    private Long expire;

    /**
     * 用户信息
     */
    private User user;

    /**
     * 用户菜单列表
     */
    private List<Menu> menus;
}