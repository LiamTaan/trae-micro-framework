package com.trae.micro.stats.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统计实体类
 */
@Data
@TableName("sys_stats")
public class Stats {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 统计日期
     */
    @TableField("stats_date")
    private LocalDateTime statsDate;

    /**
     * 总用户数
     */
    @TableField("total_users")
    private Long totalUsers;

    /**
     * 今日新增用户数
     */
    @TableField("today_new_users")
    private Long todayNewUsers;

    /**
     * 在线用户数
     */
    @TableField("online_users")
    private Long onlineUsers;

    /**
     * 系统状态
     */
    @TableField("system_status")
    private String systemStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 逻辑删除标识
     */
    @TableField("deleted")
    private Integer deleted;
}