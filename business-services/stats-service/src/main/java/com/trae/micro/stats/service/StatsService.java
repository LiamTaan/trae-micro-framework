package com.trae.micro.stats.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trae.micro.stats.model.Stats;
import com.trae.micro.stats.controller.DashboardController;

/**
 * 统计服务接口
 */
public interface StatsService extends IService<Stats> {

    /**
     * 获取最新统计数据
     */
    DashboardController.DashboardStatsResponse getLatestStats();

}