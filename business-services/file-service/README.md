# file-service 文件服务

## 1. 项目概述

file-service是Trae AI微服务框架的文件服务，负责文件的上传、下载、管理等功能。该服务独立部署，支持多种存储方式，采用策略模式设计，可灵活扩展不同的存储实现。

## 2. 核心功能

- 文件上传
- 文件下载
- 文件列表查询
- 文件删除
- 存储策略扩展
- 文件信息管理
- 跨服务文件访问

## 3. 目录结构

```
file-service/
├── src/main/java/com/trae/micro/file/
│   ├── config/          # 配置类
│   │   └── SecurityConfig.java   # 安全配置类
│   ├── controller/       # 控制器层
│   │   └── FileController.java    # 文件控制器
│   ├── mapper/           # 数据访问层
│   │   └── FileMapper.java         # 文件Mapper
│   ├── model/            # 数据模型
│   │   └── FileInfo.java           # 文件信息模型
│   ├── service/          # 业务逻辑层
│   │   └── FileService.java        # 文件服务
│   ├── strategy/         # 存储策略
│   │   ├── StorageStrategy.java     # 存储策略接口
│   │   └── StorageStrategyFactory.java  # 存储策略工厂
│   └── FileServiceApplication.java  # 服务启动类
├── src/main/resources/
│   ├── application.yml   # 应用配置
│   └── bootstrap.yml     # 引导配置
├── Dockerfile            # Docker构建文件
├── pom.xml               # Maven配置
└── README.md             # 项目说明文档
```

## 4. API接口

### 4.1 文件管理接口

| 路径 | 方法 | 功能 | 权限 |
|------|------|------|------|
| `/api/v1/file/upload` | POST | 上传文件 | 认证用户 |
| `/api/v1/file/download/{id}` | GET | 下载文件 | 认证用户 |
| `/api/v1/file/delete/{id}` | DELETE | 删除文件 | `file:delete` |
| `/api/v1/file/list` | GET | 分页查询文件列表 | `file:list` |
| `/api/v1/file/{id}` | GET | 查询文件详情 | `file:query` |

## 5. 数据模型

### 5.1 文件信息模型 (FileInfo)

| 字段名 | 类型 | 描述 |
|--------|------|------|
| `id` | Long | 文件ID |
| `file_name` | String | 文件名 |
| `file_path` | String | 文件存储路径 |
| `file_size` | Long | 文件大小（字节） |
| `file_type` | String | 文件类型 |
| `storage_type` | String | 存储类型 |
| `url` | String | 文件访问URL |
| `create_user_id` | Long | 创建用户ID |
| `create_time` | LocalDateTime | 创建时间 |
| `update_time` | LocalDateTime | 更新时间 |

## 6. 启动方式

### 6.1 环境准备

- JDK 17
- Maven 3.8.8+
- MySQL 8.0
- Redis 6.0+（可选，用于文件缓存）
- Nacos 2.3.0+

### 6.2 本地启动

1. **启动依赖服务**
   - 启动MySQL并执行`docs/sql-scripts/`目录下的SQL脚本
   - 启动Redis（可选）
   - 启动Nacos并导入`configs/nacos/file-service.yaml`配置文件

2. **编译打包**
   ```bash
   mvn clean package -DskipTests
   ```

3. **启动服务**
   ```bash
   java -jar file-service-1.0.0.jar
   ```

4. **访问服务**
   - 服务地址: http://localhost:8083
   - 接口文档: http://localhost:8083/doc.html

## 7. 部署说明

### 7.1 Docker部署

1. **构建Docker镜像**
   ```bash
   docker build -t trae-file-service:1.0.0 .
   ```

2. **运行Docker容器**
   ```bash
   docker run -d -p 8083:8083 --name file-service trae-file-service:1.0.0
   ```

### 7.2 Docker Compose部署

在项目根目录的`deploy`目录下，使用docker-compose.yml进行一键部署：

```bash
cd deploy
docker-compose up -d
```

## 8. 存储策略

### 8.1 策略设计

文件服务采用策略模式设计，支持多种存储方式，主要策略包括：

- **本地存储**：文件存储在本地磁盘
- **S3存储**：文件存储在Amazon S3或兼容S3的对象存储服务
- **FastDFS**：文件存储在FastDFS分布式文件系统
- **MinIO**：文件存储在MinIO对象存储服务

### 8.2 策略扩展

要添加新的存储策略，只需实现`StorageStrategy`接口，并在`StorageStrategyFactory`中注册即可。

示例：

```java
public class NewStorageStrategy implements StorageStrategy {
    // 实现存储策略方法
}

// 在StorageStrategyFactory中注册
storageStrategyMap.put("new", new NewStorageStrategy());
```

## 9. 安全配置

### 9.1 权限控制

- 基于JWT的认证机制
- 基于注解的权限控制
- 支持细粒度权限管理

### 9.2 访问控制

- 文件上传权限控制
- 文件下载权限控制
- 文件删除权限控制
- 文件列表访问权限控制

## 10. 接口文档

服务提供了详细的接口文档，可通过Knife4j访问：

- **Knife4j接口文档**: http://localhost:8083/doc.html

## 11. 监控与日志

### 11.1 日志配置

- 日志框架: SLF4J + Logback
- 日志级别: INFO
- 日志格式: JSON格式

### 11.2 监控指标

- 健康检查: http://localhost:8083/actuator/health
- 信息端点: http://localhost:8083/actuator/info
- 指标端点: http://localhost:8083/actuator/metrics

## 12. 常见问题

### 12.1 文件上传失败

- 检查文件大小是否超过限制
- 检查存储路径是否存在且可写
- 检查存储策略配置是否正确

### 12.2 文件下载失败

- 检查文件是否存在
- 检查文件路径是否正确
- 检查用户是否有下载权限

### 12.3 存储策略切换

- 修改配置文件中的`storage.type`属性
- 重启服务生效

## 13. 版本更新

### v1.0.0
- 初始版本
- 支持文件上传下载
- 实现存储策略设计
- 支持文件列表查询
- 实现文件删除功能

## 14. 贡献指南

1. Fork项目
2. 创建特性分支
3. 提交代码
4. 推送分支
5. 提交Pull Request

## 15. 许可证

[MIT License](LICENSE)
