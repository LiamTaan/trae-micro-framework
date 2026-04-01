package com.trae.micro.stats.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trae.micro.stats.model.Stats;
import org.apache.ibatis.annotations.Mapper;

/**
 * 统计服务Mapper接口
 */
@Mapper
public interface StatsMapper extends BaseMapper<Stats> {

}