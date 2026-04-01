# gateway API网关服务

## 1. 项目概述

gateway是Trae AI微服务框架的API网关服务，作为系统的统一入口，负责路由转发、认证鉴权、流量控制和接口文档聚合等功能。该服务独立部署，可水平扩展，支持动态路由配置。

## 2. 核心功能

- 请求路由转发
- JWT token验证
- 跨域请求处理
- 接口文档聚合
- 服务发现与负载均衡
- 流量控制与熔断
- 统一日志记录

## 3. 目录结构

```
gateway/
├── src/main/java/com/trae/micro/gateway/
│   ├── filter/         # 网关过滤器
│   │   └── JwtAuthenticationFilter.java  # JWT认证过滤器
│   └── GatewayApplication.java           # 网关启动类
├── src/main/resources/
│   ├── application.yml   # 应用配置
│   └── bootstrap.yml     # 引导配置
├── .gitignore
├── Dockerfile            # Docker构建文件
├── pom.xml               # Maven配置
└── README.md             # 项目说明文档
```

## 4. 路由配置

网关路由配置在Nacos配置中心中管理，主要路由规则如下：

### 4.1 认证服务路由
- **路径**: `/api/v1/auth/**`
- **服务**: `auth-service`
- **描述**: 处理用户登录、登出等认证相关请求

### 4.2 系统服务路由
- **路径**: `/api/v1/system/**`
- **服务**: `system-service`
- **描述**: 处理菜单、角色等系统管理相关请求

### 4.3 文件服务路由
- **路径**: `/api/v1/file/**`
- **服务**: `file-service`
- **描述**: 处理文件上传、下载等文件管理相关请求

### 4.4 接口文档路由
- **路径**: `/swagger-ui/**`, `/v3/api-docs/**`
- **服务**: `auth-service`
- **描述**: 聚合所有服务的接口文档

## 5. 启动方式

### 5.1 环境准备

- JDK 17
- Maven 3.8.8+
- Nacos 2.3.0+
- Redis 6.0+（可选，用于限流）

### 5.2 本地启动

1. **启动Nacos**
   - 确保Nacos服务已启动并运行
   - 导入`configs/nacos/gateway.yaml`配置文件

2. **编译打包**
   ```bash
   mvn clean package -DskipTests
   ```

3. **启动服务**
   ```bash
   java -jar gateway-1.0.0.jar
   ```

4. **访问网关**
   - 网关地址: http://localhost:8080
   - 接口文档: http://localhost:8080/doc.html

## 6. 部署说明

### 6.1 Docker部署

1. **构建Docker镜像**
   ```bash
   docker build -t trae-gateway:1.0.0 .
   ```

2. **运行Docker容器**
   ```bash
   docker run -d -p 8080:8080 --name gateway trae-gateway:1.0.0
   ```

### 6.2 Docker Compose部署

在项目根目录的`deploy`目录下，使用docker-compose.yml进行一键部署：

```bash
cd deploy
docker-compose up -d
```

## 7. 安全配置

### 7.1 JWT认证

网关使用JWT token进行认证，主要配置：
- JWT secret: 用于签名和验证token
- token过期时间: 默认3600秒
- 排除认证路径: 如登录接口、注册接口等

### 7.2 跨域配置

网关支持跨域请求，主要配置：
- 允许的Origin: *（生产环境建议配置为具体域名）
- 允许的Methods: GET, POST, PUT, DELETE, OPTIONS
- 允许的Headers: *
- 允许携带凭证: true

## 8. 接口文档

网关聚合了所有服务的接口文档，可通过以下地址访问：

- **Knife4j接口文档**: http://localhost:8080/doc.html

## 9. 监控与日志

### 9.1 日志配置

网关日志使用SLF4J + Logback，主要日志级别：
- 网关核心: DEBUG
- Spring Cloud Gateway: INFO
- 业务日志: INFO

### 9.2 监控指标

网关支持Prometheus监控，可通过以下端点获取指标：
- **Prometheus端点**: http://localhost:8080/actuator/prometheus
- **健康检查**: http://localhost:8080/actuator/health

## 10. 常见问题

### 10.1 路由转发失败

- 检查服务是否已注册到Nacos
- 检查路由配置是否正确
- 查看网关日志，定位具体错误信息

### 10.2 JWT认证失败

- 检查token是否有效
- 检查token是否过期
- 检查JWT secret是否一致

### 10.3 跨域请求失败

- 检查跨域配置是否正确
- 确认请求方法和头信息是否符合要求

## 11. 版本更新

### v1.0.0
- 初始版本
- 支持路由转发
- 实现JWT认证
- 支持跨域请求
- 聚合接口文档

## 12. 贡献指南

1. Fork项目
2. 创建特性分支
3. 提交代码
4. 推送分支
5. 提交Pull Request

## 13. 许可证

[MIT License](LICENSE)
