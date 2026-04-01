package com.trae.micro.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.core.security.TenantContextHolder;
import com.trae.micro.system.mapper.RoleMapper;
import com.trae.micro.system.model.Role;
import com.trae.micro.system.service.RoleService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final JdbcTemplate jdbcTemplate;

    public RoleServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PageResult<Role> page(PageQuery pageQuery) {
        Page<Role> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Role> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        // 可以添加条件查询
        if (pageQuery.getKeyword() != null && !pageQuery.getKeyword().isEmpty()) {
            queryWrapper.like("name", pageQuery.getKeyword())
                    .or().like("code", pageQuery.getKeyword())
                    .or().like("description", pageQuery.getKeyword());
        }
        
        // 分页查询
        Page<Role> resultPage = baseMapper.selectPage(page, queryWrapper);
        
        return PageResult.of(resultPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenus(Long roleId, List<Long> menuIds) {
        Long tenantId = TenantContextHolder.getTenantId();
        // 先删除原有关联
        jdbcTemplate.update("DELETE FROM sys_role_menu WHERE role_id = ? AND tenant_id = ?", roleId, tenantId);
        
        // 保存新关联
        if (menuIds != null && !menuIds.isEmpty()) {
            String sql = "INSERT INTO sys_role_menu (role_id, menu_id, tenant_id) VALUES (?, ?, ?)";
            for (Long menuId : menuIds) {
                jdbcTemplate.update(sql, roleId, menuId, tenantId);
            }
        }
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        Long tenantId = TenantContextHolder.getTenantId();
        String sql = "SELECT menu_id FROM sys_role_menu WHERE role_id = ? AND tenant_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, roleId, tenantId);
    }
}