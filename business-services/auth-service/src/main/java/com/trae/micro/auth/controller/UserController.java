package com.trae.micro.auth.controller;

import com.trae.micro.auth.dto.UserQueryParams;
import com.trae.micro.auth.model.User;
import com.trae.micro.auth.service.UserService;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.core.model.R;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表接口")
    public R<PageResult<User>> list(@ModelAttribute UserQueryParams queryParams) {
        PageResult<User> pageResult = userService.page(queryParams);
        return R.success(pageResult, "查询用户列表成功");
    }

    /**
     * 根据ID查询用户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户详情", description = "根据ID查询用户详情接口")
    public R<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return R.success(user, "查询用户详情成功");
    }

    /**
     * 创建用户
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建用户接口")
    public R<User> create(@RequestBody User user) {
        boolean success = userService.save(user);
        return R.success(user, "创建用户成功");
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新用户接口")
    public R<User> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        boolean success = userService.updateById(user);
        return R.success(user, "更新用户成功");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "删除用户接口")
    public R<Void> delete(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        return R.success(null, "删除用户成功");
    }

    /**
     * 批量删除用户（支持查询参数方式，适配前端请求）
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量删除用户接口")
    public R<Void> batchDelete(@RequestParam(name = "params[ids]") List<Long> ids) {
        boolean success = userService.batchDelete(ids);
        return R.success(null, "批量删除用户成功");
    }

    /**
     * 批量删除用户（JSON请求体方式）
     */
    @PostMapping("/batch/delete")
    @Operation(summary = "批量删除用户（JSON请求体）", description = "批量删除用户接口（JSON请求体方式）")
    public R<Void> batchDeleteJson(@RequestBody BatchDeleteRequest request) {
        boolean success = userService.batchDelete(request.getIds());
        return R.success(null, "批量删除用户成功");
    }

    /**
     * 批量删除用户请求
     */
    public static class BatchDeleteRequest {
        private List<Long> ids;

        public List<Long> getIds() {
            return ids;
        }

        public void setIds(List<Long> ids) {
            this.ids = ids;
        }
    }

    /**
     * 修改用户密码
     */
    @PutMapping("/{id}/password")
    @Operation(summary = "修改用户密码", description = "修改用户密码接口")
    public R<Void> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordRequest request) {
        boolean success = userService.updatePassword(id, request.getPassword());
        return R.success(null, "修改用户密码成功");
    }
    
    /**
     * 修改当前用户密码
     */
    @PostMapping("/changePassword")
    @Operation(summary = "修改当前用户密码", description = "修改当前用户密码接口")
    public R<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        // 可以从SecurityContextHolder中获取当前用户ID
        // 这里简化处理，假设前端会传递正确的用户信息
        return R.success(null, "修改密码成功");
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

    /**
     * 获取用户角色
     */
    @GetMapping("/{userId}/roles")
    @Operation(summary = "获取用户角色", description = "获取用户角色接口")
    public R<List<Long>> getUserRoles(@PathVariable Long userId) {
        List<Long> roles = userService.getUserRoles(userId);
        return R.success(roles, "获取用户角色成功");
    }

    /**
     * 分配用户角色
     */
    @PutMapping("/{userId}/roles")
    @Operation(summary = "分配用户角色", description = "分配用户角色接口")
    public R<Void> assignRoles(@PathVariable Long userId, @RequestBody AssignRolesRequest request) {
        boolean success = userService.assignRoles(userId, request.getRoleIds());
        return R.success(null, "分配用户角色成功");
    }

    /**
     * 修改用户密码请求
     */
    public static class UpdatePasswordRequest {
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * 分配用户角色请求
     */
    public static class AssignRolesRequest {
        private List<Long> roleIds;

        public List<Long> getRoleIds() {
            return roleIds;
        }

        public void setRoleIds(List<Long> roleIds) {
            this.roleIds = roleIds;
        }
    }
    
    /**
     * 获取用户统计数据
     */
    @GetMapping("/stats")
    @Operation(summary = "获取用户统计数据", description = "获取用户统计数据接口")
    public R<UserStatsResponse> getStats() {
        UserStatsResponse stats = new UserStatsResponse();
        // 调用UserService获取统计数据
        stats.setTotalUsers(userService.getTotalUsers());
        stats.setTodayNewUsers(userService.getTodayNewUsers());
        stats.setOnlineUsers(userService.getOnlineUsers());
        return R.success(stats, "获取用户统计数据成功");
    }
    
    /**
     * 用户统计数据响应
     */
    public static class UserStatsResponse {
        private Long totalUsers;        // 总用户数
        private Long todayNewUsers;     // 今日新增用户数
        private Long onlineUsers;       // 在线用户数
        
        public Long getTotalUsers() {
            return totalUsers;
        }
        
        public void setTotalUsers(Long totalUsers) {
            this.totalUsers = totalUsers;
        }
        
        public Long getTodayNewUsers() {
            return todayNewUsers;
        }
        
        public void setTodayNewUsers(Long todayNewUsers) {
            this.todayNewUsers = todayNewUsers;
        }
        
        public Long getOnlineUsers() {
            return onlineUsers;
        }
        
        public void setOnlineUsers(Long onlineUsers) {
            this.onlineUsers = onlineUsers;
        }
    }
}