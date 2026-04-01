package com.trae.micro.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trae.micro.core.model.Menu;
import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.system.mapper.MenuMapper;
import com.trae.micro.system.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> getMenuListByUserId(Long userId) {
        return baseMapper.selectMenuListByUserId(userId);
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        // 先过滤出顶级菜单（parentId为0或null）
        List<Menu> rootMenus = menus.stream()
                .filter(menu -> menu.getParentId() == null || menu.getParentId() == 0)
                .sorted((m1, m2) -> {
                    Integer sort1 = m1.getSort() != null ? m1.getSort() : 0;
                    Integer sort2 = m2.getSort() != null ? m2.getSort() : 0;
                    return sort1.compareTo(sort2);
                })
                .collect(Collectors.toList());

        // 递归构建子菜单
        for (Menu rootMenu : rootMenus) {
            buildChildrenMenus(rootMenu, menus);
        }

        return rootMenus;
    }

    @Override
    public PageResult<Menu> page(PageQuery pageQuery) {
        Page<Menu> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Menu> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        // 可以添加条件查询
        if (pageQuery.getKeyword() != null && !pageQuery.getKeyword().isEmpty()) {
            queryWrapper.like("name", pageQuery.getKeyword())
                    .or().like("path", pageQuery.getKeyword())
                    .or().like("perms", pageQuery.getKeyword());
        }
        
        // 按排序字段排序
        queryWrapper.orderByAsc("sort");
        
        // 分页查询
        Page<Menu> resultPage = baseMapper.selectPage(page, queryWrapper);
        
        return PageResult.of(resultPage);
    }

    @Override
    public List<Menu> getAllMenus() {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return removeByIds(ids);
    }

    /**
     * 递归构建子菜单
     */
    private void buildChildrenMenus(Menu parentMenu, List<Menu> allMenus) {
        List<Menu> childrenMenus = allMenus.stream()
                .filter(menu -> menu.getParentId() != null && menu.getParentId().equals(parentMenu.getId()))
                .sorted((m1, m2) -> {
                    Integer sort1 = m1.getSort() != null ? m1.getSort() : 0;
                    Integer sort2 = m2.getSort() != null ? m2.getSort() : 0;
                    return sort1.compareTo(sort2);
                })
                .collect(Collectors.toList());

        if (!childrenMenus.isEmpty()) {
            parentMenu.setChildren(childrenMenus);
            for (Menu childMenu : childrenMenus) {
                buildChildrenMenus(childMenu, allMenus);
            }
        }
    }
}