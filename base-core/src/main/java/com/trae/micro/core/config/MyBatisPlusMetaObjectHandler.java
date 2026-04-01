package com.trae.micro.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.trae.micro.core.security.SecurityContextHolder;
import com.trae.micro.core.security.TenantContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 元对象处理器，用于自动填充创建时间、更新时间、创建人、更新人等字段
 */
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 设置创建时间
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        // 设置更新时间
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        // 设置创建人
        Long userId = SecurityContextHolder.getCurrentUser() != null ? SecurityContextHolder.getCurrentUser().getUserId() : 0L;
        this.setFieldValByName("createBy", userId, metaObject);
        // 设置更新人
        this.setFieldValByName("updateBy", userId, metaObject);
        // 设置租户ID
        Long tenantId = TenantContextHolder.getTenantId() != null ? TenantContextHolder.getTenantId() : 0L;
        this.setFieldValByName("tenantId", tenantId, metaObject);
    }

    /**
     * 更新时自动填充
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 设置更新时间
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        // 设置更新人
        Long userId = SecurityContextHolder.getCurrentUser() != null ? SecurityContextHolder.getCurrentUser().getUserId() : 0L;
        this.setFieldValByName("updateBy", userId, metaObject);
    }
}
