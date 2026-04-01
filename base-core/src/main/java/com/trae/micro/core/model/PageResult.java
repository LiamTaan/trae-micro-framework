package com.trae.micro.core.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 每页记录数
     */
    private Long pageSize;

    /**
     * 总页数
     */
    private Long totalPages;

    /**
     * 当前页数
     */
    private Long currentPage;

    /**
     * 列表数据
     */
    private List<T> records;

    /**
     * 转换为分页结果
     */
    public static <T> PageResult<T> of(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPageSize(page.getSize());
        result.setTotalPages(page.getPages());
        result.setCurrentPage(page.getCurrent());
        result.setRecords(page.getRecords());
        return result;
    }

    /**
     * 转换为分页结果
     */
    public static <T, R> PageResult<R> of(Page<T> page, List<R> records) {
        PageResult<R> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPageSize(page.getSize());
        result.setTotalPages(page.getPages());
        result.setCurrentPage(page.getCurrent());
        result.setRecords(records);
        return result;
    }
}