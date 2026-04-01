# auth-service 认证服务

## 1. 项目概述

auth-service是Trae AI微服务框架的认证服务，负责用户认证与授权，包括登录、登出、用户管理等功能。该服务独立部署，使用JWT进行身份验证，支持基于角色的权限控制。

## 2. 核心功能

- 用户登录/登出
- JWT token生成与验证
- 用户信息管理
- 密码加密与验证
- 用户状态管理
- 最后登录时间记录
- 跨服务调用认证

## 3. 目录结构

```
auth-service/
├── src/main/java/com/trae/micro/auth/
│   ├── controller/       # 控制器层
│   │   ├── AuthController.java  # 认证控制器
│   │   └── UserController.java   # 用户管理控制器
│   ├── dto/              # 数据传输对象
│   │   ├── LoginRequest.java     # 登录请求DTO
│   │   └── LoginResponse.java    # 登录响应DTO
│   ├── feign/            # Feign客户端
│   │   └── SystemFeignClient.java  # 系统服务Feign客户端
│   ├── mapper/           # 数据访问层
│   │   └── UserMapper.java        # 用户Mapper
│   ├── model/            # 数据模型
│   │   └── User.java             # 用户模型
│   ├── security/         # 安全配置
│   │   └── SecurityConfig.java   # 安全配置类
│   ├── service/          # 业务逻辑层
│   │   └── UserService.java      # 用户服务
│   └── AuthServiceApplication.java  # 服务启动类
├── src/main/resources/
│   ├── application.yml   # 应用配置
│   └── bootstrap.yml     # 引导配置
├── .gitignore
├── Dockerfile            # Docker构建文件
├── pom.xml               # Maven配置
└── README.md             # 项目说明文档
```

## 4. API接口

### 4.1 认证接口

| 路径 | 方法 | 功能 | 权限 |
|------|------|------|------|
| `/api/v1/auth/login` | POST | 用户登录 | 公开 |
| `/api/v1/auth/logout` | POST | 用户登出 | 认证用户 |

### 4.2 用户管理接口

| 路径 | 方法 | 功能 | 权限 |
|------|------|------|------|
| `/api/v1/users` | GET | 分页查询用户列表 | `user:list` |
| `/api/v1/users/{id}` | GET | 根据ID查询用户详情 | `user:query` |
| `/api/v1/users` | POST | 创建用户 | `user:add` |
| `/api/v1/users/{id}` | PUT | 更新用户信息 | `user:update` |
| `/api/v1/users/{id}` | DELETE | 删除用户 | `user:delete` |

## 5. 数据模型

### 5.1 用户模型 (User)

| 字段名 | 类型 | 描述 |
|--------|------|------|
| `id` | Long | 用户ID |
| `username` | String | 用户名 |
| `password` | String | 密码（加密存储） |
| `nickname` | String | 昵称 |
| `email` | String | 邮箱 |
| `phone` | String | 手机号 |
| `avatar` | String | 头像 |
| `gender` | Integer | 性别（1-男，0-女） |
| `status` | Integer | 状态（1-启用，0-禁用） |
| `lastLoginTime` | LocalDateTime | 最后登录时间 |
| `createTime` | LocalDateTime | 创建时间 |
| `updateTime` | LocalDateTime | 更新时间 |

## 6. 启动方式

### 6.1 环境准备

- JDK 17
- Maven 3.8.8+
- MySQL 8.0
- Redis 6.0+
- Nacos 2.3.0+

### 6.2 本地启动

1. **启动依赖服务**
   - 启动MySQL并执行`docs/sql-scripts/`目录下的SQL脚本
   - 启动Redis
   - 启动Nacos并导入`configs/nacos/auth-service.yaml`配置文件

2. **编译打包**
   ```bash
   mvn clean package -DskipTests
   ```

3. **启动服务**
   ```bash
   java -jar auth-service-1.0.0.jar
   ```

4. **访问服务**
   - 服务地址: http://localhost:8081
   - 接口文档: http://localhost:8081/doc.html

## 7. 部署说明

### 7.1 Docker部署

1. **构建Docker镜像**
   ```bash
   docker build -t trae-auth-service:1.0.0 .
   ```

2. **运行Docker容器**
   ```bash
   docker run -d -p 8081:8081 --name auth-service trae-auth-service:1.0.0
   ```

### 7.2 Docker Compose部署

在项目根目录的`deploy`目录下，使用docker-compose.yml进行一键部署：

```bash
cd deploy
docker-compose up -d
```

## 8. 安全配置

### 8.1 JWT配置

- JWT secret: 用于签名和验证token
- token过期时间: 默认3600秒
- 刷新token机制: 支持token刷新

### 8.2 密码加密

- 加密算法: BCrypt
- 密码强度: 10轮迭代

### 8.3 权限控制

- 基于JWT的认证机制
- 基于注解的权限控制
- 支持细粒度权限管理

## 9. 接口文档

服务提供了详细的接口文档，可通过Knife4j访问：

- **Knife4j接口文档**: http://localhost:8081/doc.html

## 10. 监控与日志

### 10.1 日志配置

- 日志框架: SLF4J + Logback
- 日志级别: INFO
- 日志格式: JSON格式

### 10.2 监控指标

- 健康检查: http://localhost:8081/actuator/health
- 信息端点: http://localhost:8081/actuator/info
- 指标端点: http://localhost:8081/actuator/metrics

## 11. 常见问题

### 11.1 登录失败

- 检查用户名和密码是否正确
- 检查用户状态是否启用
- 检查JWT配置是否正确

### 11.2 权限验证失败

- 检查用户是否拥有所需权限
- 检查权限注解是否正确配置
- 检查token是否有效

## 12. 版本更新

### v1.0.0
- 初始版本
- 支持用户登录/登出
- 实现JWT认证
- 支持用户管理
- 实现密码加密

## 13. 贡献指南

1. Fork项目
2. 创建特性分支
3. 提交代码
4. 推送分支
5. 提交Pull Request

## 14. 许可证

[MIT License](LICENSE)
