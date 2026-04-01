package com.trae.micro.stats.service.impl;

import com.trae.micro.stats.controller.DashboardController;
import com.trae.micro.stats.service.DashboardService;
import com.trae.micro.stats.service.StatsService;
import org.springframework.stereotype.Service;

/**
 * 仪表盘服务实现类
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final StatsService statsService;

    public DashboardServiceImpl(StatsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public DashboardController.DashboardStatsResponse getStats() {
        // 调用StatsService获取真实数据
        return statsService.getLatestStats();
    }

}