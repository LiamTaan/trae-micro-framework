package com.trae.micro.auth.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trae.micro.auth.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus自动实现基本CRUD
    
    /**
     * 获取用户角色ID列表
     */
    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId} AND tenant_id = #{tenantId}")
    List<Long> getUserRoleIds(@Param("userId") Long userId, @Param("tenantId") Long tenantId);
    
    /**
     * 删除用户角色关联
     */
    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId} AND tenant_id = #{tenantId}")
    int deleteUserRoles(@Param("userId") Long userId, @Param("tenantId") Long tenantId);
    
    /**
     * 插入用户角色关联
     */
    @Insert("INSERT INTO sys_user_role (user_id, role_id, tenant_id) VALUES (#{userId}, #{roleId}, #{tenantId})")
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId, @Param("tenantId") Long tenantId);
    
    /**
     * 根据用户名查询用户（不使用租户过滤）
     * 用于登录认证时查询所有租户的用户
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT id, username, password, nickname, email, phone, avatar, gender, status, last_login_time, tenant_id, create_time, update_time, deleted FROM sys_user WHERE deleted = 0 AND username = #{username}")
    User getByUsernameWithoutTenantFilter(@Param("username") String username);

    /**
     * 根据ID更新用户（使用租户过滤）
     * 用于更新用户信息时过滤租户
     */
    @Update("UPDATE sys_user SET last_login_time = #{lastLoginTime} WHERE id = #{id} AND tenant_id = #{tenantId}")
    void updateByIdAndTenantId(User user);
}