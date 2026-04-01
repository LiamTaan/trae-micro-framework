package com.trae.micro.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.system.model.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    /**
     * 分页查询角色列表
     *
     * @param pageQuery 分页查询条件
     * @return 分页结果
     */
    PageResult<Role> page(PageQuery pageQuery);

    /**
     * 保存角色菜单关联
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     */
    void saveRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 根据角色ID获取菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);
}