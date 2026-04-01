-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS trae_micro_framework DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS trae_micro_framework_0 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS trae_micro_framework_1 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS trae_micro_framework_2 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE trae_micro_framework;

-- 创建租户表
CREATE TABLE IF NOT EXISTS sys_tenant (
    tenant_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '租户ID',
    tenant_name VARCHAR(100) NOT NULL COMMENT '租户名称',
    tenant_code VARCHAR(50) NOT NULL COMMENT '租户编码',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    description VARCHAR(255) COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by BIGINT DEFAULT 0 COMMENT '创建人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    update_by BIGINT DEFAULT 0 COMMENT '更新人',
    UNIQUE KEY uk_tenant_code (tenant_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表';

-- 创建用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号码',
    avatar VARCHAR(255) COMMENT '头像',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY uk_tenant_username (tenant_id, username),
    KEY idx_status (status),
    KEY idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建菜单表
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    type TINYINT NOT NULL COMMENT '菜单类型：0-目录，1-菜单，2-按钮',
    path VARCHAR(100) COMMENT '路由地址',
    component VARCHAR(255) COMMENT '组件路径',
    perms VARCHAR(100) COMMENT '权限标识',
    icon VARCHAR(50) COMMENT '图标',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    visible TINYINT DEFAULT 1 COMMENT '是否可见：0-不可见，1-可见',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    KEY idx_parent_id (parent_id),
    KEY idx_type (type),
    KEY idx_status (status),
    KEY idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 创建角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色代码',
    description VARCHAR(255) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 创建角色菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES sys_menu(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 创建文件表
CREATE TABLE IF NOT EXISTS sys_file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_path VARCHAR(255) NOT NULL COMMENT '文件路径',
    file_size BIGINT NOT NULL COMMENT '文件大小（字节）',
    file_type VARCHAR(100) COMMENT '文件类型',
    file_suffix VARCHAR(20) COMMENT '文件后缀',
    user_id BIGINT COMMENT '上传用户ID',
    user_name VARCHAR(50) COMMENT '上传用户名称',
    md5 VARCHAR(32) COMMENT '文件MD5值',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- 创建统计数据表
CREATE TABLE `sys_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `stats_date` datetime NOT NULL COMMENT '统计日期',
  `total_users` bigint DEFAULT '0' COMMENT '总用户数',
  `today_new_users` bigint DEFAULT '0' COMMENT '今日新增用户数',
  `online_users` bigint DEFAULT '0' COMMENT '在线用户数',
  `system_status` varchar(20) DEFAULT '正常' COMMENT '系统状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除标识（0：未删除，1：已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_stats_date` (`stats_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计数据表';

-- 为其他表添加租户ID字段
ALTER TABLE sys_menu ADD COLUMN tenant_id BIGINT DEFAULT 0 COMMENT '租户ID' AFTER id;
ALTER TABLE sys_role ADD COLUMN tenant_id BIGINT DEFAULT 0 COMMENT '租户ID' AFTER id;
ALTER TABLE sys_file ADD COLUMN tenant_id BIGINT DEFAULT 0 COMMENT '租户ID' AFTER id;
-- 为文件表添加file_url列
ALTER TABLE sys_file ADD COLUMN file_url VARCHAR(255) COMMENT '文件URL地址' AFTER file_path;
-- 为文件表添加审计字段
ALTER TABLE sys_file ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人' AFTER create_time;
ALTER TABLE sys_file ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人' AFTER update_time;
-- 为用户表添加审计字段
ALTER TABLE sys_user ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人' AFTER update_time;
ALTER TABLE sys_user ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人' AFTER create_by;
-- 为菜单表添加审计字段
ALTER TABLE sys_menu ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人' AFTER update_time;
ALTER TABLE sys_menu ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人' AFTER create_by;
-- 为角色表添加审计字段
ALTER TABLE sys_role ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人' AFTER update_time;
ALTER TABLE sys_role ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人' AFTER create_by;

-- 为关联表添加租户ID字段
ALTER TABLE sys_role_menu ADD COLUMN tenant_id BIGINT DEFAULT 0 COMMENT '租户ID' AFTER role_id;
ALTER TABLE sys_user_role ADD COLUMN tenant_id BIGINT DEFAULT 0 COMMENT '租户ID' AFTER user_id;

-- 为关联表添加审计字段
ALTER TABLE sys_role_menu ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人' AFTER tenant_id;
ALTER TABLE sys_role_menu ADD COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER create_by;
ALTER TABLE sys_role_menu ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人' AFTER create_time;
ALTER TABLE sys_role_menu ADD COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER update_by;

ALTER TABLE sys_user_role ADD COLUMN create_by BIGINT DEFAULT 0 COMMENT '创建人' AFTER tenant_id;
ALTER TABLE sys_user_role ADD COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER create_by;
ALTER TABLE sys_user_role ADD COLUMN update_by BIGINT DEFAULT 0 COMMENT '更新人' AFTER create_time;
ALTER TABLE sys_user_role ADD COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER update_by;

-- 添加索引
CREATE INDEX idx_menu_tenant_id ON sys_menu(tenant_id);
CREATE INDEX idx_role_tenant_id ON sys_role(tenant_id);
CREATE INDEX idx_file_tenant_id ON sys_file(tenant_id);
CREATE INDEX idx_role_menu_tenant_id ON sys_role_menu(tenant_id);
CREATE INDEX idx_user_role_tenant_id ON sys_user_role(tenant_id);