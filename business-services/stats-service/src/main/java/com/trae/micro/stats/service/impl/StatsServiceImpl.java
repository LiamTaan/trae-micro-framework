package com.trae.micro.stats.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trae.micro.stats.controller.DashboardController;
import com.trae.micro.stats.feign.UserFeignClient;
import com.trae.micro.stats.mapper.StatsMapper;
import com.trae.micro.stats.model.Stats;
import com.trae.micro.stats.service.StatsService;
import org.springframework.stereotype.Service;

/**
 * 统计服务实现类
 */
@Service
public class StatsServiceImpl extends ServiceImpl<StatsMapper, Stats> implements StatsService {

    private final UserFeignClient userFeignClient;

    public StatsServiceImpl(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    @Override
    public DashboardController.DashboardStatsResponse getLatestStats() {
        // 通过Feign调用auth-service获取真实的用户统计数据
        var userStatsResponse = userFeignClient.getUserStats();
        
        DashboardController.DashboardStatsResponse response = new DashboardController.DashboardStatsResponse();
        
        if (userStatsResponse != null && userStatsResponse.getSuccess() && userStatsResponse.getData() != null) {
            var userStats = userStatsResponse.getData();
            response.setTotalUsers(userStats.getTotalUsers());
            response.setTodayNewUsers(userStats.getTodayNewUsers());
            response.setOnlineUsers(userStats.getOnlineUsers());
            response.setSystemStatus("正常");
        } else {
            // 如果调用失败，返回默认值
            response.setTotalUsers(0L);
            response.setTodayNewUsers(0L);
            response.setOnlineUsers(0L);
            response.setSystemStatus("正常");
        }
        
        return response;
    }

}