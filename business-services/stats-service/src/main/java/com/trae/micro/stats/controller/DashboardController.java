package com.trae.micro.stats.controller;

import com.trae.micro.core.model.R;
import com.trae.micro.stats.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "仪表盘管理", description = "仪表盘相关接口")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/stats")
    @Operation(summary = "获取仪表盘统计数据", description = "获取仪表盘统计数据接口")
    public R<DashboardStatsResponse> getStats() {
        DashboardStatsResponse stats = dashboardService.getStats();
        return R.success(stats, "获取统计数据成功");
    }

    /**
     * 仪表盘统计数据响应
     */
    public static class DashboardStatsResponse {
        private Long totalUsers;        // 总用户数
        private Long todayNewUsers;     // 今日新增用户数
        private Long onlineUsers;       // 在线用户数
        private String systemStatus;    // 系统状态

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

        public String getSystemStatus() {
            return systemStatus;
        }

        public void setSystemStatus(String systemStatus) {
            this.systemStatus = systemStatus;
        }
    }
}