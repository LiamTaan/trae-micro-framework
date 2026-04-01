package com.trae.micro.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileService extends IService<FileInfo> {
    /**
     * 文件上传
     *
     * @param file 文件
     * @param userId 用户ID
     * @param userName 用户名
     * @return 文件信息
     * @throws IOException IO异常
     */
    FileInfo upload(MultipartFile file, Long userId, String userName) throws IOException;

    /**
     * 文件下载
     *
     * @param fileId 文件ID
     * @param response 响应
     * @throws IOException IO异常
     */
    void download(Long fileId, HttpServletResponse response) throws IOException;

    /**
     * 文件删除
     *
     * @param fileId 文件ID
     * @return 是否删除成功
     */
    boolean delete(Long fileId);

    /**
     * 批量文件上传
     *
     * @param files 文件列表
     * @param userId 用户ID
     * @param userName 用户名
     * @return 文件信息列表
     * @throws IOException IO异常
     */
    List<FileInfo> batchUpload(MultipartFile[] files, Long userId, String userName) throws IOException;

    /**
     * 根据用户ID查询文件列表
     *
     * @param userId 用户ID
     * @return 文件列表
     */
    List<FileInfo> getFileListByUserId(Long userId);

    /**
     * 分页查询文件列表
     *
     * @param pageQuery 分页查询条件
     * @return 分页结果
     */
    PageResult<FileInfo> page(PageQuery pageQuery);
    
    /**
     * 批量删除文件
     *
     * @param ids 文件ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);
}