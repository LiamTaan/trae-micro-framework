package com.trae.micro.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trae.micro.auth.dto.UserQueryParams;
import com.trae.micro.auth.mapper.UserMapper;
import com.trae.micro.auth.model.User;
import com.trae.micro.auth.service.UserService;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.core.security.TenantContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getByUsername(String username) {
        // 登录时不使用租户过滤，直接查询所有租户的用户
        return baseMapper.getByUsernameWithoutTenantFilter(username);
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void updateLastLoginTime(Long userId, Long tenantId) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        baseMapper.updateByIdAndTenantId(user);
    }

    @Override
    public PageResult<User> page(UserQueryParams queryParams) {
        Page<User> page = new Page<>(queryParams.getPageNum(), queryParams.getPageSize());
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        // 处理用户名查询
        if (queryParams.getUsername() != null && !queryParams.getUsername().isEmpty()) {
            queryWrapper.like("username", queryParams.getUsername());
        }
        
        // 处理昵称查询
        if (queryParams.getNickname() != null && !queryParams.getNickname().isEmpty()) {
            queryWrapper.like("nickname", queryParams.getNickname());
        }
        
        // 处理状态查询
        if (queryParams.getStatus() != null) {
            queryWrapper.eq("status", queryParams.getStatus());
        }
        
        // 处理创建时间范围查询
        if (queryParams.getCreateTimeStart() != null) {
            queryWrapper.ge("create_time", queryParams.getCreateTimeStart());
        }
        if (queryParams.getCreateTimeEnd() != null) {
            queryWrapper.le("create_time", queryParams.getCreateTimeEnd());
        }
        
        // 处理关键字查询（兼容原有逻辑）
        if (queryParams.getKeyword() != null && !queryParams.getKeyword().isEmpty()) {
            queryWrapper.like("username", queryParams.getKeyword())
                    .or().like("nickname", queryParams.getKeyword())
                    .or().like("email", queryParams.getKeyword());
        }
        
        // 分页查询
        Page<User> resultPage = baseMapper.selectPage(page, queryWrapper);
        
        return PageResult.of(resultPage);
    }

    @Override
    public boolean save(User user) {
        // 密码加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return super.save(user);
    }

    @Override
    public boolean updateById(User user) {
        // 如果密码不为空，则加密后保存
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 否则不更新密码
            user.setPassword(null);
        }
        return super.updateById(user);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public boolean updatePassword(Long userId, String password) {
        User user = new User();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(password));
        // 直接调用mapper的updateById方法，绕过service层的updateById，避免密码被二次加密
        return super.updateById(user);
    }

    @Override
    public List<Long> getUserRoles(Long userId) {
        Long tenantId = TenantContextHolder.getTenantId();
        return baseMapper.getUserRoleIds(userId, tenantId);
    }

    @Override
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        Long tenantId = TenantContextHolder.getTenantId();
        // 先删除用户原有的角色
        baseMapper.deleteUserRoles(userId, tenantId);
        
        // 保存新角色
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                baseMapper.insertUserRole(userId, roleId, tenantId);
            }
        }
        
        return true;
    }
}