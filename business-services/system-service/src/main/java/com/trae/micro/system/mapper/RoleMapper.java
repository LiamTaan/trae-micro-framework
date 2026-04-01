package com.trae.micro.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trae.micro.system.model.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    // MyBatis-Plus自动实现基本CRUD
}