# system-service 系统管理服务

## 1. 项目概述

system-service是Trae AI微服务框架的系统管理服务，负责系统基础数据管理，包括菜单管理、角色管理、权限分配等功能。该服务独立部署，提供细粒度的权限控制，支持动态路由生成。

## 2. 核心功能

- 菜单管理（动态路由）
- 角色管理与权限分配
- 用户-角色关联管理
- 角色-菜单关联管理
- 权限资源管理
- 系统配置管理
- 数据字典管理
- 部门管理

## 3. 目录结构

```
system-service/
├── src/main/java/com/trae/micro/system/
│   ├── config/          # 配置类
│   │   └── SecurityConfig.java   # 安全配置类
│   ├── controller/       # 控制器层
│   │   ├── MenuController.java    # 菜单控制器
│   │   └── RoleController.java    # 角色控制器
│   ├── mapper/           # 数据访问层
│   │   ├── MenuMapper.java         # 菜单Mapper
│   │   └── RoleMapper.java         # 角色Mapper
│   ├── model/            # 数据模型
│   │   ├── Menu.java              # 菜单模型
│   │   └── Role.java              # 角色模型
│   ├── service/          # 业务逻辑层
│   │   ├── MenuService.java        # 菜单服务
│   │   └── RoleService.java        # 角色服务
│   └── SystemServiceApplication.java  # 服务启动类
├── src/main/resources/
│   ├── mapper/          # MyBatis映射文件
│   │   └── MenuMapper.xml         # 菜单Mapper映射文件
│   ├── application.yml   # 应用配置
│   └── bootstrap.yml     # 引导配置
├── .gitignore
├── Dockerfile            # Docker构建文件
├── pom.xml               # Maven配置
└── README.md             # 项目说明文档
```

## 4. API接口

### 4.1 菜单管理接口

| 路径 | 方法 | 功能 | 权限 |
|------|------|------|------|
| `/api/v1/system/menus` | GET | 分页查询菜单列表 | `system:menu:list` |
| `/api/v1/system/menus/tree` | GET | 获取菜单树 | `system:menu:query` |
| `/api/v1/system/menus/{id}` | GET | 查询菜单详情 | `system:menu:query` |
| `/api/v1/system/menus` | POST | 创建菜单 | `system:menu:add` |
| `/api/v1/system/menus/{id}` | PUT | 更新菜单 | `system:menu:update` |
| `/api/v1/system/menus/{id}` | DELETE | 删除菜单 | `system:menu:delete` |
| `/api/v1/system/menus/user/{userId}` | GET | 根据用户ID获取菜单列表 | 认证用户 |

### 4.2 角色管理接口

| 路径 | 方法 | 功能 | 权限 |
|------|------|------|------|
| `/api/v1/system/roles` | GET | 分页查询角色列表 | `system:role:list` |
| `/api/v1/system/roles/{id}` | GET | 查询角色详情 | `system:role:query` |
| `/api/v1/system/roles` | POST | 创建角色 | `system:role:add` |
| `/api/v1/system/roles/{id}` | PUT | 更新角色 | `system:role:update` |
| `/api/v1/system/roles/{id}` | DELETE | 删除角色 | `system:role:delete` |
| `/api/v1/system/roles/{id}/menus` | GET | 获取角色菜单ID列表 | `system:role:query` |
| `/api/v1/system/roles/{id}/menus` | POST | 保存角色菜单关联 | `system:role:update` |

## 5. 数据模型

### 5.1 菜单模型 (Menu)

| 字段名 | 类型 | 描述 |
|--------|------|------|
| `id` | Long | 菜单ID |
| `parent_id` | Long | 父菜单ID |
| `name` | String | 菜单名称 |
| `type` | Integer | 菜单类型（0-目录，1-菜单，2-按钮） |
| `path` | String | 路由路径 |
| `component` | String | 组件路径 |
| `perms` | String | 权限标识 |
| `icon` | String | 菜单图标 |
| `sort` | Integer | 排序 |
| `status` | Integer | 状态（1-启用，0-禁用） |
| `visible` | Integer | 是否可见（1-可见，0-隐藏） |
| `create_time` | LocalDateTime | 创建时间 |
| `update_time` | LocalDateTime | 更新时间 |

### 5.2 角色模型 (Role)

| 字段名 | 类型 | 描述 |
|--------|------|------|
| `id` | Long | 角色ID |
| `name` | String | 角色名称 |
| `code` | String | 角色编码 |
| `description` | String | 角色描述 |
| `status` | Integer | 状态（1-启用，0-禁用） |
| `create_time` | LocalDateTime | 创建时间 |
| `update_time` | LocalDateTime | 更新时间 |

## 6. 启动方式

### 6.1 环境准备

- JDK 17
- Maven 3.8.8+
- MySQL 8.0
- Redis 6.0+（可选，用于缓存）
- Nacos 2.3.0+

### 6.2 本地启动

1. **启动依赖服务**
   - 启动MySQL并执行`docs/sql-scripts/`目录下的SQL脚本
   - 启动Redis（可选）
   - 启动Nacos并导入`configs/nacos/system-service.yaml`配置文件

2. **编译打包**
   ```bash
   mvn clean package -DskipTests
   ```

3. **启动服务**
   ```bash
   java -jar system-service-1.0.0.jar
   ```

4. **访问服务**
   - 服务地址: http://localhost:8082
   - 接口文档: http://localhost:8082/doc.html

## 7. 部署说明

### 7.1 Docker部署

1. **构建Docker镜像**
   ```bash
   docker build -t trae-system-service:1.0.0 .
   ```

2. **运行Docker容器**
   ```bash
   docker run -d -p 8082:8082 --name system-service trae-system-service:1.0.0
   ```

### 7.2 Docker Compose部署

在项目根目录的`deploy`目录下，使用docker-compose.yml进行一键部署：

```bash
cd deploy
docker-compose up -d
```

## 8. 安全配置

### 8.1 权限控制

- 基于JWT的认证机制
- 基于注解的权限控制
- 支持细粒度权限管理
- 动态权限校验

### 8.2 数据安全

- 敏感数据加密存储
- 数据访问权限控制
- 操作日志记录

## 9. 接口文档

服务提供了详细的接口文档，可通过Knife4j访问：

- **Knife4j接口文档**: http://localhost:8082/doc.html

## 10. 监控与日志

### 10.1 日志配置

- 日志框架: SLF4J + Logback
- 日志级别: INFO
- 日志格式: JSON格式

### 10.2 监控指标

- 健康检查: http://localhost:8082/actuator/health
- 信息端点: http://localhost:8082/actuator/info
- 指标端点: http://localhost:8082/actuator/metrics

## 11. 常见问题

### 11.1 菜单不显示

- 检查用户是否分配了对应的角色
- 检查角色是否关联了菜单
- 检查菜单状态是否启用
- 检查菜单是否可见

### 11.2 权限验证失败

- 检查用户是否拥有所需权限
- 检查权限注解是否正确配置
- 检查角色-菜单关联是否正确

### 11.3 动态路由生成失败

- 检查菜单配置是否正确
- 检查路由路径是否符合Vue Router规范
- 检查组件路径是否存在

## 12. 版本更新

### v1.0.0
- 初始版本
- 支持菜单管理（动态路由）
- 支持角色管理与权限分配
- 实现用户-角色关联管理
- 实现角色-菜单关联管理

## 13. 贡献指南

1. Fork项目
2. 创建特性分支
3. 提交代码
4. 推送分支
5. 提交Pull Request

## 14. 许可证

[MIT License](LICENSE)
