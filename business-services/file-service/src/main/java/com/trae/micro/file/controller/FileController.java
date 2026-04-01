package com.trae.micro.file.controller;

import com.trae.micro.core.model.PageQuery;
import com.trae.micro.core.model.PageResult;
import com.trae.micro.file.model.FileInfo;
import com.trae.micro.file.service.FileService;
import com.trae.micro.core.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/file")
@Tag(name = "文件管理", description = "文件相关接口")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "文件上传接口")
    public R<FileInfo> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") @Parameter(description = "用户ID") Long userId,
            @RequestParam("userName") @Parameter(description = "用户名") String userName) throws IOException {
        log.info("文件上传请求：{}，用户ID：{}，用户名：{}", file.getOriginalFilename(), userId, userName);
        FileInfo fileInfo = fileService.upload(file, userId, userName);
        return R.success(fileInfo, "文件上传成功");
    }

    /**
     * 批量文件上传
     */
    @PostMapping("/batch-upload")
    @Operation(summary = "批量文件上传", description = "批量文件上传接口")
    public R<List<FileInfo>> batchUpload(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("userId") @Parameter(description = "用户ID") Long userId,
            @RequestParam("userName") @Parameter(description = "用户名") String userName) throws IOException {
        log.info("批量文件上传请求，文件数量：{}，用户ID：{}，用户名：{}", files.length, userId, userName);
        List<FileInfo> fileInfoList = fileService.batchUpload(files, userId, userName);
        return R.success(fileInfoList, "文件批量上传成功");
    }

    /**
     * 文件下载
     */
    @GetMapping("/download/{fileId}")
    @Operation(summary = "文件下载", description = "文件下载接口")
    public void download(
            @PathVariable("fileId") @Parameter(description = "文件ID") Long fileId,
            HttpServletResponse response) throws IOException {
        log.info("文件下载请求：{}，用户ID：{}", fileId);
        fileService.download(fileId, response);
    }

    /**
     * 文件删除
     */
    @DeleteMapping("/{fileId}")
    @Operation(summary = "文件删除", description = "文件删除接口")
    public R<Boolean> delete(
            @PathVariable("fileId") @Parameter(description = "文件ID") Long fileId) {
        log.info("文件删除请求：{}", fileId);
        boolean result = fileService.delete(fileId);
        return R.success(result, "文件删除成功");
    }

    /**
     * 获取用户文件列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户文件列表", description = "获取用户文件列表接口")
    public R<List<FileInfo>> getFileListByUserId(
            @PathVariable("userId") @Parameter(description = "用户ID") Long userId) {
        log.info("获取用户文件列表请求：{}", userId);
        List<FileInfo> fileInfoList = fileService.getFileListByUserId(userId);
        return R.success(fileInfoList, "获取用户文件列表成功");
    }

    /**
     * 分页查询文件列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询文件列表", description = "分页查询文件列表接口")
    public R<PageResult<FileInfo>> page(@ModelAttribute PageQuery pageQuery) {
        log.info("分页查询文件列表请求：{}", pageQuery);
        PageResult<FileInfo> pageResult = fileService.page(pageQuery);
        return R.success(pageResult, "分页查询文件列表成功");
    }
}