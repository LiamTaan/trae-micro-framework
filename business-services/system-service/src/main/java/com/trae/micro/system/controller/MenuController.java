package com.trae.micro.system.controller;

import com.trae.micro.core.model.Menu;
import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.core.model.R;
import com.trae.micro.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/menus")
@Tag(name = "菜单管理", description = "菜单相关接口")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 根据用户ID获取菜单列表
     */
    @GetMapping("/user")
    @Operation(summary = "根据用户ID获取菜单列表", description = "根据用户ID获取菜单列表接口")
    public R<List<Menu>> getMenuListByUserId(@RequestParam(name = "userId") Long userId) {
        List<Menu> menuList = menuService.getMenuListByUserId(userId);
        List<Menu> menuTree = menuService.buildMenuTree(menuList);
        return R.success(menuTree, "获取菜单列表成功");
    }

    /**
     * 获取所有菜单列表（树形结构）
     */
    @GetMapping
    @Operation(summary = "获取所有菜单列表", description = "获取所有菜单列表接口")
    public R<List<Menu>> getAllMenus() {
        List<Menu> menuList = menuService.getAllMenus();
        List<Menu> menuTree = menuService.buildMenuTree(menuList);
        return R.success(menuTree, "获取所有菜单列表成功");
    }

    /**
     * 分页查询菜单列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询菜单列表", description = "分页查询菜单列表接口")
    public R<PageResult<Menu>> page(@ModelAttribute PageQuery pageQuery) {
        PageResult<Menu> pageResult = menuService.page(pageQuery);
        return R.success(pageResult, "分页查询菜单列表成功");
    }

    /**
     * 根据ID查询菜单详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询菜单详情", description = "根据ID查询菜单详情接口")
    public R<Menu> getById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return R.success(menu, "查询菜单详情成功");
    }

    /**
     * 创建菜单
     */
    @PostMapping
    @Operation(summary = "创建菜单", description = "创建菜单接口")
    public R<Menu> create(@RequestBody Menu menu) {
        boolean success = menuService.save(menu);
        return R.success(menu, "创建菜单成功");
    }

    /**
     * 更新菜单
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新菜单", description = "更新菜单接口")
    public R<Menu> update(@PathVariable Long id, @RequestBody Menu menu) {
        menu.setId(id);
        boolean success = menuService.updateById(menu);
        return R.success(menu, "更新菜单成功");
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单", description = "删除菜单接口")
    public R<Void> delete(@PathVariable Long id) {
        boolean success = menuService.removeById(id);
        return R.success(null, "删除菜单成功");
    }

    /**
     * 批量删除菜单（支持查询参数方式，适配前端请求）
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除菜单", description = "批量删除菜单接口")
    public R<Void> batchDelete(@RequestParam(name = "params[ids]") List<Long> ids) {
        boolean success = menuService.batchDelete(ids);
        return R.success(null, "批量删除菜单成功");
    }

    /**
     * 批量删除菜单（JSON请求体方式）
     */
    @PostMapping("/batch")
    @Operation(summary = "批量删除菜单（JSON请求体）", description = "批量删除菜单接口（JSON请求体方式）")
    public R<Void> batchDeleteJson(@RequestBody BatchDeleteRequest request) {
        boolean success = menuService.batchDelete(request.getIds());
        return R.success(null, "批量删除菜单成功");
    }

    /**
     * 批量删除菜单请求
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
}