package com.trae.micro.core.security;

import com.trae.micro.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Pointcut("@annotation(com.trae.micro.core.security.RequiresPermission)")
    public void permissionPointCut() {
    }

    @Around("permissionPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取当前用户信息
        SecurityContextHolder.UserDetails currentUser = SecurityContextHolder.getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException(401, "用户未登录");
        }

        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 获取权限注解
        RequiresPermission requiresPermission = method.getAnnotation(RequiresPermission.class);
        if (requiresPermission != null) {
            String permission = requiresPermission.value();
            String logical = requiresPermission.logical();

            // 验证权限
            if (!hasPermission(currentUser, permission, logical)) {
                throw new BusinessException(403, "没有操作权限");
            }
        }

        // 执行原方法
        return point.proceed();
    }

    /**
     * 验证用户是否有指定权限
     */
    private boolean hasPermission(SecurityContextHolder.UserDetails userDetails, String permission, String logical) {
        // 获取用户权限列表
        String[] userPermissions = userDetails.getPermissions();
        if (userPermissions == null || userPermissions.length == 0) {
            return false;
        }

        // 简单实现：检查用户权限列表中是否包含指定权限
        for (String userPermission : userPermissions) {
            if (permission.equals(userPermission)) {
                return true;
            }
        }

        return false;
    }
}