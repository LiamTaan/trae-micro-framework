package com.trae.micro.auth.dto;

import com.trae.micro.core.model.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryParams extends PageQuery {
    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 创建时间范围开始
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建时间范围结束
     */
    private LocalDateTime createTimeEnd;
}