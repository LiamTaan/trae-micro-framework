package com.trae.micro.file.strategy.impl;

import com.trae.micro.file.model.FileInfo;
import com.trae.micro.file.strategy.StorageStrategy;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 本地存储策略实现
 */
@Component("local")
public class LocalStorageStrategy implements StorageStrategy {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.base-url:http://localhost:8083/api/v1/file/download}")
    private String baseUrl;

    @Override
    public FileInfo upload(MultipartFile file, FileInfo fileInfo) throws IOException {
        // 获取文件信息
        String originalName = file.getOriginalFilename();
        String fileSuffix = FilenameUtils.getExtension(originalName);
        String fileName = UUID.randomUUID().toString() + "." + fileSuffix;

        // 创建上传目录
        String yearMonth = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM"));
        String filePath = uploadPath + File.separator + yearMonth;
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存文件
        Path savePath = Paths.get(filePath, fileName);
        file.transferTo(savePath);

        // 更新文件信息
        fileInfo.setFileName(fileName);
        fileInfo.setFilePath(yearMonth + File.separator + fileName);
        return fileInfo;
    }

    @Override
    public void download(FileInfo fileInfo, HttpServletResponse response) throws IOException {
        // 读取文件并写入响应
        Path filePath = Paths.get(uploadPath, fileInfo.getFilePath());
        try (InputStream inputStream = Files.newInputStream(filePath);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024 * 1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        }
    }

    @Override
    public boolean delete(FileInfo fileInfo) throws IOException {
        // 删除物理文件
        Path filePath = Paths.get(uploadPath, fileInfo.getFilePath());
        return Files.deleteIfExists(filePath);
    }

    @Override
    public String generateFileUrl(FileInfo fileInfo) {
        // 本地存储的文件URL基于文件ID
        return baseUrl + "/" + fileInfo.getId();
    }
}