package com.trae.micro.file.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.file.mapper.FileMapper;
import com.trae.micro.file.model.FileInfo;
import com.trae.micro.file.service.FileService;
import com.trae.micro.file.strategy.StorageStrategyFactory;
import com.trae.micro.core.exception.BusinessException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileInfo> implements FileService {

    private final StorageStrategyFactory storageStrategyFactory;

    public FileServiceImpl(StorageStrategyFactory storageStrategyFactory) {
        this.storageStrategyFactory = storageStrategyFactory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfo upload(MultipartFile file, Long userId, String userName) throws IOException {
        // 验证文件
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 获取文件信息
        String originalName = file.getOriginalFilename();
        String fileSuffix = FilenameUtils.getExtension(originalName);
        String fileType = file.getContentType();
        Long fileSize = file.getSize();

        // 计算文件MD5
        String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());

        // 创建文件信息对象
        FileInfo fileInfo = new FileInfo();
        fileInfo.setOriginalName(originalName);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileType(fileType);
        fileInfo.setFileSuffix(fileSuffix);
        fileInfo.setUserId(userId);
        fileInfo.setUserName(userName);
        fileInfo.setMd5(md5);
        fileInfo.setStatus(1);

        // 使用存储策略上传文件
        fileInfo = storageStrategyFactory.getCurrentStrategy().upload(file, fileInfo);

        // 保存文件信息到数据库
        baseMapper.insert(fileInfo);

        // 生成文件URL（针对本地存储）
        if (fileInfo.getFileUrl() == null) {
            String fileUrl = storageStrategyFactory.getCurrentStrategy().generateFileUrl(fileInfo);
            fileInfo.setFileUrl(fileUrl);
            // 更新文件信息
            baseMapper.updateById(fileInfo);
        }

        return fileInfo;
    }

    @Override
    public void download(Long fileId, HttpServletResponse response) throws IOException {
        // 查询文件信息
        FileInfo fileInfo = baseMapper.selectById(fileId);
        if (fileInfo == null || fileInfo.getStatus() == 0) {
            throw new BusinessException("文件不存在或已禁用");
        }

        // 设置响应头
        response.setContentType(fileInfo.getFileType());
        response.setHeader("Content-Disposition", "attachment; filename=" + fileInfo.getOriginalName());
        response.setHeader("Content-Length", String.valueOf(fileInfo.getFileSize()));

        // 使用存储策略下载文件
        storageStrategyFactory.getCurrentStrategy().download(fileInfo, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long fileId) {
        // 查询文件信息
        FileInfo fileInfo = baseMapper.selectById(fileId);
        if (fileInfo == null) {
            throw new BusinessException("文件不存在");
        }

        // 删除物理文件
        try {
            storageStrategyFactory.getCurrentStrategy().delete(fileInfo);
        } catch (IOException e) {
            log.error("删除文件失败：{}", e.getMessage(), e);
            // 物理文件删除失败不影响数据库记录删除
        }

        // 删除数据库记录
        return baseMapper.deleteById(fileId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FileInfo> batchUpload(MultipartFile[] files, Long userId, String userName) throws IOException {
        List<FileInfo> fileInfoList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                FileInfo fileInfo = upload(file, userId, userName);
                fileInfoList.add(fileInfo);
            }
        }
        return fileInfoList;
    }

    @Override
    public List<FileInfo> getFileListByUserId(Long userId) {
        return baseMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FileInfo>()
                .eq("user_id", userId)
                .eq("status", 1)
                .orderByDesc("create_time"));
    }

    @Override
    public PageResult<FileInfo> page(PageQuery pageQuery) {
        Page<FileInfo> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FileInfo> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        // 可以添加条件查询
        if (pageQuery.getKeyword() != null && !pageQuery.getKeyword().isEmpty()) {
            queryWrapper.like("original_name", pageQuery.getKeyword());
        }
        
        // 只查询启用的文件
        queryWrapper.eq("status", 1);
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc("create_time");
        
        // 分页查询
        Page<FileInfo> resultPage = baseMapper.selectPage(page, queryWrapper);
        
        return PageResult.of(resultPage);
    }
}