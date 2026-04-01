package com.trae.micro.file.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 存储策略工厂
 */
@Component
public class StorageStrategyFactory {

    @Value("${file.storage.type:local}")
    private String storageType;

    private final Map<String, StorageStrategy> storageStrategies;

    @Autowired
    public StorageStrategyFactory(Map<String, StorageStrategy> storageStrategies) {
        this.storageStrategies = storageStrategies;
    }

    /**
     * 获取当前存储策略
     *
     * @return 当前存储策略
     */
    public StorageStrategy getCurrentStrategy() {
        StorageStrategy strategy = storageStrategies.get(storageType);
        if (strategy == null) {
            // 默认使用本地存储策略
            strategy = storageStrategies.get("local");
        }
        return strategy;
    }

    /**
     * 根据类型获取存储策略
     *
     * @param type 存储类型
     * @return 存储策略
     */
    public StorageStrategy getStrategyByType(String type) {
        StorageStrategy strategy = storageStrategies.get(type);
        if (strategy == null) {
            // 默认使用本地存储策略
            strategy = storageStrategies.get("local");
        }
        return strategy;
    }
}