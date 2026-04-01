package com.trae.micro.file.strategy.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.trae.micro.file.model.FileInfo;
import com.trae.micro.file.strategy.StorageStrategy;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OSS存储策略实现
 */
@Component("oss")
public class OssStorageStrategy implements StorageStrategy {

    @Value("${oss.endpoint:}")
    private String endpoint;

    @Value("${oss.accessKeyId:}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret:}")
    private String accessKeySecret;

    @Value("${oss.bucketName:}")
    private String bucketName;

    @Value("${oss.domain:}")
    private String domain;

    private void validateOssConfig() {
        if (endpoint == null || endpoint.isEmpty()) {
            throw new IllegalArgumentException("OSS endpoint is required");
        }
        if (accessKeyId == null || accessKeyId.isEmpty()) {
            throw new IllegalArgumentException("OSS accessKeyId is required");
        }
        if (accessKeySecret == null || accessKeySecret.isEmpty()) {
            throw new IllegalArgumentException("OSS accessKeySecret is required");
        }
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalArgumentException("OSS bucketName is required");
        }
    }

    @Override
    public FileInfo upload(MultipartFile file, FileInfo fileInfo) throws IOException {
        // 验证OSS配置
        validateOssConfig();
        
        // 创建OSS客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 获取文件信息
            String originalName = file.getOriginalFilename();
            String fileSuffix = FilenameUtils.getExtension(originalName);
            String fileName = UUID.randomUUID().toString() + "." + fileSuffix;

            // 创建文件路径
            String yearMonth = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM"));
            String objectName = yearMonth + "/" + fileName;

            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // 上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file.getInputStream(), metadata);
            ossClient.putObject(putObjectRequest);

            // 构建文件URL
            String fileUrl = domain + "/" + objectName;

            // 更新文件信息
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(objectName);
            fileInfo.setFileUrl(fileUrl);

            return fileInfo;
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }
    }

    @Override
    public void download(FileInfo fileInfo, HttpServletResponse response) throws IOException {
        // 验证OSS配置
        validateOssConfig();
        
        // 创建OSS客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 获取文件流
            InputStream inputStream = ossClient.getObject(bucketName, fileInfo.getFilePath()).getObjectContent();

            // 设置响应头
            response.setContentType(fileInfo.getFileType());
            response.setHeader("Content-Disposition", "attachment; filename=" + fileInfo.getOriginalName());
            response.setHeader("Content-Length", String.valueOf(fileInfo.getFileSize()));

            // 写入响应
            try (OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[1024 * 1024];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
            } finally {
                inputStream.close();
            }
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }
    }

    @Override
    public boolean delete(FileInfo fileInfo) throws IOException {
        // 验证OSS配置
        validateOssConfig();
        
        // 创建OSS客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 删除文件
            ossClient.deleteObject(bucketName, fileInfo.getFilePath());
            return true;
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }
    }
}