package com.trae.micro.file.strategy;

import com.trae.micro.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件存储策略接口
 */
public interface StorageStrategy {
    /**
     * 上传文件
     *
     * @param file      上传的文件
     * @param fileInfo  文件信息对象
     * @return 更新后的文件信息
     * @throws IOException IO异常
     */
    FileInfo upload(MultipartFile file, FileInfo fileInfo) throws IOException;

    /**
     * 下载文件
     *
     * @param fileInfo 文件信息
     * @param response 响应对象
     * @throws IOException IO异常
     */
    void download(FileInfo fileInfo, HttpServletResponse response) throws IOException;

    /**
     * 删除文件
     *
     * @param fileInfo 文件信息
     * @return 是否删除成功
     * @throws IOException IO异常
     */
    boolean delete(FileInfo fileInfo) throws IOException;

    /**
     * 生成文件URL
     *
     * @param fileInfo 文件信息
     * @return 文件URL
     */
    default String generateFileUrl(FileInfo fileInfo) {
        return null;
    }
}