package com.trae.micro.core.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageQuery<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Long pageNum = 1L;

    /**
     * 每页条数
     */
    private Long pageSize = 10L;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式：asc/desc
     */
    private String sortOrder;

    /**
     * 关键字查询
     */
    private String keyword;

    /**
     * 转换为MyBatis Plus的Page对象
     */
    public <E> Page<E> toPage() {
        Page<E> page = new Page<>(pageNum, pageSize);
        return page;
    }
}