package com.trae.micro.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务类，用于管理在线用户和其他Redis操作，所有服务共享
 */
@Service
public class RedisService {

    private static final String ONLINE_USER_KEY = "online:users";
    private static final String USER_TOKEN_KEY_PREFIX = "user:token:";
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 添加在线用户
     * @param userId 用户ID
     * @param token 令牌
     * @param expire 过期时间（毫秒）
     */
    public void addOnlineUser(Long userId, String token, Long expire) {
        // 添加用户到在线集合
        redisTemplate.opsForSet().add(ONLINE_USER_KEY, userId);
        // 存储用户token关系，用于登出时删除
        redisTemplate.opsForValue().set(USER_TOKEN_KEY_PREFIX + userId, token, expire, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 移除在线用户
     * @param userId 用户ID
     */
    public void removeOnlineUser(Long userId) {
        // 从在线集合中移除用户
        redisTemplate.opsForSet().remove(ONLINE_USER_KEY, userId);
        // 删除用户token关系
        redisTemplate.delete(USER_TOKEN_KEY_PREFIX + userId);
    }
    
    /**
     * 获取在线用户数量
     * @return 在线用户数量
     */
    public Long getOnlineUserCount() {
        return redisTemplate.opsForSet().size(ONLINE_USER_KEY);
    }
    
    /**
     * 获取在线用户ID列表
     * @return 在线用户ID列表
     */
    public Set<Object> getOnlineUserIds() {
        return redisTemplate.opsForSet().members(ONLINE_USER_KEY);
    }
    
    /**
     * 检查用户是否在线
     * @param userId 用户ID
     * @return 是否在线
     */
    public Boolean isUserOnline(Long userId) {
        return redisTemplate.opsForSet().isMember(ONLINE_USER_KEY, userId);
    }
    
    /**
     * 设置Redis键值对
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    /**
     * 设置Redis键值对，带过期时间
     * @param key 键
     * @param value 值
     * @param expire 过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }
    
    /**
     * 获取Redis值
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 删除Redis键
     * @param key 键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    /**
     * 判断Redis键是否存在
     * @param key 键
     * @return 是否存在
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
}