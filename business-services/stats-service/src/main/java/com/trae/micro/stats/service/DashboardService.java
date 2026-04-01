package com.trae.micro.stats.service;

import com.trae.micro.stats.controller.DashboardController;

/**
 * 仪表盘服务接口
 */
public interface DashboardService {

    /**
     * 获取仪表盘统计数据
     * @return 仪表盘统计数据
     */
    DashboardController.DashboardStatsResponse getStats();

}