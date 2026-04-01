package com.trae.micro.system.controller;

import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.core.model.R;
import com.trae.micro.system.model.Role;
import com.trae.micro.system.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "角色管理", description = "角色相关接口")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 分页查询角色列表
     */
    @GetMapping
    @Operation(summary = "分页查询角色列表", description = "分页查询角色列表接口")
    public R<PageResult<Role>> list(@ModelAttribute PageQuery pageQuery) {
        PageResult<Role> pageResult = roleService.page(pageQuery);
        return R.success(pageResult, "查询角色列表成功");
    }

    /**
     * 根据ID查询角色详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询角色详情", description = "根据ID查询角色详情接口")
    public R<Role> getById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return R.success(role, "查询角色详情成功");
    }

    /**
     * 创建角色
     */
    @PostMapping
    @Operation(summary = "创建角色", description = "创建角色接口")
    public R<Role> create(@RequestBody Role role) {
        boolean success = roleService.save(role);
        return R.success(role, "创建角色成功");
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新角色", description = "更新角色接口")
    public R<Role> update(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        boolean success = roleService.updateById(role);
        return R.success(role, "更新角色成功");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "删除角色接口")
    public R<Void> delete(@PathVariable Long id) {
        boolean success = roleService.removeById(id);
        return R.success(null, "删除角色成功");
    }
    
    /**
     * 批量删除角色
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除角色", description = "批量删除角色接口")
    public R<Void> batchDelete(@RequestParam(name = "ids") List<Long> ids) {
        boolean success = roleService.removeByIds(ids);
        return R.success(null, "批量删除角色成功");
    }

    /**
     * 获取角色菜单ID列表
     */
    @GetMapping("/{id}/menus")
    @Operation(summary = "获取角色菜单ID列表", description = "获取角色菜单ID列表接口")
    public R<List<Long>> getRoleMenuIds(@PathVariable Long id) {
        List<Long> menuIds = roleService.getMenuIdsByRoleId(id);
        return R.success(menuIds, "获取角色菜单ID列表成功");
    }

    /**
     * 保存角色菜单关联
     */
    @PostMapping("/{id}/menus")
    @Operation(summary = "保存角色菜单关联", description = "保存角色菜单关联接口")
    public R<Void> saveRoleMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        roleService.saveRoleMenus(id, menuIds);
        return R.success(null, "保存角色菜单关联成功");
    }
}