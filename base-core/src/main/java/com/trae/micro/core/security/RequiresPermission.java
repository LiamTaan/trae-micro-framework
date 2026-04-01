package com.trae.micro.core.security;

import java.lang.annotation.*;

/**
 * 权限注解，用于方法级别的权限控制
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {
    /**
     * 权限标识
     */
    String value();

    /**
     * 逻辑关系：and/or，默认为and
     */
    String logical() default "and";
}