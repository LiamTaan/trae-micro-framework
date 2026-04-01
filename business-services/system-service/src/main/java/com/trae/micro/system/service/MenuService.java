package com.trae.micro.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trae.micro.core.model.Menu;
import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;

import java.util.List;

public interface MenuService extends IService<Menu> {
    /**
     * 根据用户ID查询菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> getMenuListByUserId(Long userId);

    /**
     * 获取菜单树
     *
     * @param menus 菜单列表
     * @return 菜单树
     */
    List<Menu> buildMenuTree(List<Menu> menus);

    /**
     * 分页查询菜单列表
     *
     * @param pageQuery 分页查询条件
     * @return 分页结果
     */
    PageResult<Menu> page(PageQuery pageQuery);

    /**
     * 获取所有菜单列表（不分页）
     *
     * @return 菜单列表
     */
    List<Menu> getAllMenus();

    /**
     * 批量删除菜单
     *
     * @param ids 菜单ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);
}