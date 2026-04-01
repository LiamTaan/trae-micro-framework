package com.trae.micro.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trae.micro.file.model.FileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<FileInfo> {
}