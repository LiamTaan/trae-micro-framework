package com.trae.micro.core.security;

/**
 * 租户上下文持有者，用于存储和获取当前租户信息
 */
public class TenantContextHolder {
    private static final ThreadLocal<Long> TENANT_CONTEXT = new ThreadLocal<>();

    /**
     * 设置当前租户ID
     * @param tenantId 租户ID
     */
    public static void setTenantId(Long tenantId) {
        TENANT_CONTEXT.set(tenantId);
    }

    /**
     * 获取当前租户ID
     * @return 租户ID
     */
    public static Long getTenantId() {
        return TENANT_CONTEXT.get();
    }

    /**
     * 清除当前租户ID
     */
    public static void clearTenantId() {
        TENANT_CONTEXT.remove();
    }

    /**
     * 判断是否设置了租户ID
     * @return 是否设置了租户ID
     */
    public static boolean hasTenantId() {
        return TENANT_CONTEXT.get() != null;
    }
}
