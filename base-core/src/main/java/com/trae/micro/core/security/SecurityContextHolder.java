package com.trae.micro.core.security;

import lombok.Getter;
import lombok.Setter;

/**
 * 安全上下文持有者，用于存储当前用户信息
 */
public class SecurityContextHolder {
    private static final ThreadLocal<UserDetails> USER_DETAILS = new ThreadLocal<>();

    /**
     * 获取当前用户信息
     *
     * @return 当前用户信息
     */
    public static UserDetails getCurrentUser() {
        return USER_DETAILS.get();
    }

    /**
     * 设置当前用户信息
     *
     * @param userDetails 用户信息
     */
    public static void setCurrentUser(UserDetails userDetails) {
        USER_DETAILS.set(userDetails);
    }

    /**
     * 清除当前用户信息
     */
    public static void clearCurrentUser() {
        USER_DETAILS.remove();
    }

    /**
     * 用户详情类
     */
    @Getter
    @Setter
    public static class UserDetails {
        /**
         * 用户ID
         */
        private Long userId;

        /**
         * 用户名
         */
        private String username;

        /**
         * 昵称
         */
        private String nickname;

        /**
         * 邮箱
         */
        private String email;

        /**
         * 角色列表
         */
        private String[] roles;

        /**
         * 权限列表
         */
        private String[] permissions;

        public UserDetails() {
        }

        public UserDetails(Long userId, String username) {
            this.userId = userId;
            this.username = username;
        }
    }
}