package com.trae.micro.core.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.trae.micro.core.security.TenantContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 多租户SQL解析器
 */
public class TenantSqlParser implements TenantLineHandler {

    /**
     * 获取租户ID列名
     */
    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }

    /**
     * 获取当前租户ID
     */
    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            tenantId = 1L; // 默认使用默认租户ID
        }
        return new LongValue(tenantId);
    }

    /**
     * 是否忽略租户过滤
     * @param tableName 表名
     */
    @Override
    public boolean ignoreTable(String tableName) {
        // 只忽略租户表本身，其他系统表需要租户过滤
        return "sys_tenant".equals(tableName);
    }


}
