package com.trae.micro.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trae.micro.auth.model.User;
import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 验证密码
     *
     * @param user 用户信息
     * @param password 密码
     * @return 是否验证通过
     */
    boolean verifyPassword(User user, String password);

    /**
     * 更新用户登录时间
     *
     * @param userId 用户ID
     */
    void updateLastLoginTime(Long userId, Long tenantId );

    /**
     * 分页查询用户列表
     *
     * @param queryParams 查询条件
     * @return 分页结果
     */
    PageResult<User> page(com.trae.micro.auth.dto.UserQueryParams queryParams);

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 修改用户密码
     *
     * @param userId 用户ID
     * @param password 新密码
     * @return 是否修改成功
     */
    boolean updatePassword(Long userId, String password);

    /**
     * 获取用户角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getUserRoles(Long userId);

    /**
     * 分配用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否分配成功
     */
    boolean assignRoles(Long userId, List<Long> roleIds);
}