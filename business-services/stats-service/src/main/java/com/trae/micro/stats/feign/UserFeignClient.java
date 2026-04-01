package com.trae.micro.stats.feign;

import com.trae.micro.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "auth-service")
public interface UserFeignClient {

    /**
     * 获取用户统计数据
     */
    @GetMapping("/api/v1/users/stats")
    R<UserStatsResponse> getUserStats();
    
    /**
     * 用户统计数据响应
     */
    class UserStatsResponse {
        private Long totalUsers;        // 总用户数
        private Long todayNewUsers;     // 今日新增用户数
        private Long onlineUsers;       // 在线用户数
        
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
    }

}