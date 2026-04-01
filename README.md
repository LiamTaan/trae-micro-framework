# Trae AI 微服务框架

## 1. 项目概述

前后端分离、可独立部署的微服务框架，基于Spring Cloud Alibaba构建，支持多服务独立部署和统一管理。框架包含3个独立仓库，支持各业务模块独立打包部署，前端与后端解耦。

### 核心特性

- ✅ 前后端分离架构，支持独立部署
- ✅ 多服务独立打包，模块化设计
- ✅ 完整的权限管理体系（基于Spring Security）
- ✅ 多租户隔离机制
- ✅ 数据库分库分表、读写分离支持
- ✅ 完善的审计日志（create_by、update_by等字段）
- ✅ 统一的监控告警体系
- ✅ Docker容器化部署，支持一键启动
- ✅ 完善的基础功能（登录、用户管理、菜单管理）

## 2. 技术栈

### 后端

- **基础框架**: Java 17 + Spring Boot 3.x
- **微服务框架**: Spring Cloud Alibaba
- **持久层**: MyBatis Plus
- **安全认证**: JWT
- **注册中心/配置中心**: Nacos
- **缓存**: Redis
- **数据库**: MySQL 8.0
- **接口文档**: Knife4j
- **流量控制**: Sentinel
- **部署**: Docker

### 前端

- **框架**: Vue 3 + Element Plus
- **构建工具**: Vite
- **状态管理**: Pinia

## 3. 项目结构（3个独立仓库）

### 仓库A（后端核心 - trae-micro-framework）

```
trae-micro-framework/
├── base-core/              # 公共核心包，所有服务依赖
│   ├── src/main/java/com/trae/micro/core/
│   │   ├── config/        # 基础配置
│   │   ├── exception/     # 异常处理
│   │   ├── feign/         # Feign客户端配置
│   │   ├── handler/       # 全局处理器
│   │   ├── model/         # 通用模型
│   │   └── security/      # 安全组件
│   └── pom.xml
├── business-services/      # 业务服务目录
│   ├── auth-service/      # 认证服务（独立微服务）
│   │   ├── .gitignore
│   │   ├── Dockerfile     # 独立Dockerfile
│   │   └── pom.xml
│   ├── file-service/      # 文件服务（独立微服务）
│   │   ├── Dockerfile     # 独立Dockerfile
│   │   └── pom.xml
│   └── system-service/    # 系统管理服务（独立微服务）
│       ├── .gitignore
│       ├── Dockerfile     # 独立Dockerfile
│       └── pom.xml
├── gateway/               # API网关服务（独立部署）
│   ├── .gitignore
│   ├── Dockerfile         # 独立Dockerfile
│   └── pom.xml
├── configs/               # 配置文件
│   └── nacos/             # Nacos配置
├── deploy/                # 部署脚本
│   ├── docker-compose.yml
│   ├── start-all.bat
│   └── start-all.sh
├── docs/                  # 文档
│   └── sql-scripts/       # SQL脚本
├── logs/                  # 日志目录
└── pom.xml               # 根父POM
```

### 各服务功能说明

#### base-core（公共核心包）

- **功能**：所有微服务的基础依赖包，提供通用功能和组件
- **核心组件**：
  - 异常处理机制
  - 全局响应处理器
  - JWT认证组件
  - 权限控制注解
  - 分页查询模型
  - Feign客户端配置
  - 基础安全配置

#### gateway（API网关）

- **功能**：系统统一入口，负责路由转发、认证鉴权、流量控制
- **核心功能**：
  - 请求路由转发
  - JWT token验证
  - 跨域请求处理
  - 接口文档聚合

#### auth-service（认证服务）

- **功能**：用户认证与授权服务，负责登录、登出、用户管理
- **核心功能**：
  - 用户登录/登出
  - JWT token生成与验证
  - 用户信息管理
  - 密码加密与验证

#### system-service（系统管理服务）

- **功能**：系统基础管理服务，负责菜单、角色、权限管理
- **核心功能**：
  - 菜单管理（动态路由）
  - 角色管理与权限分配
  - 用户-角色关联管理
  - 角色-菜单关联管理

#### file-service（文件服务）

- **功能**：文件上传下载服务，支持多种存储方式
- **核心功能**：
  - 文件上传
  - 文件下载
  - 文件列表查询
  - 存储策略扩展

### 仓库B（前端独立 - trae-micro-frontend）

```
trae-micro-frontend/
├── public/                # 静态资源
├── src/
│   ├── assets/           # 资源文件
│   ├── components/       # 通用组件
│   ├── router/           # 路由配置
│   ├── stores/           # Pinia状态管理
│   ├── utils/            # 工具函数
│   ├── views/            # 页面组件
│   ├── App.vue           # 根组件
│   └── main.ts           # 入口文件
├── .env.development      # 开发环境配置
├── .env.production       # 生产环境配置
├── index.html
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

### 仓库C（全栈聚合 - trae-micro-deploy）

```
trae-micro-deploy/
├── docker-compose.yml     # 一键部署脚本
├── .env                   # 环境变量配置
└── README.md              # 部署说明
```

## 4. 快速启动

### 环境准备

- JDK 17
- Maven 3.8.8+
- Node.js 16+
- MySQL 8.0
- Redis 6.0+
- Nacos 2.3.0+
- Docker (可选)

### 后端启动步骤

1. **启动依赖服务**
   - 启动MySQL，创建数据库（建议名称：micro\_framework）
   - 执行`docs/sql-scripts/`目录下的SQL脚本：
     - `create_tables.sql`：创建所有表结构
     - `init_data.sql`：初始化基础数据（管理员账号、角色、菜单等）
   - 启动Redis
   - 启动Nacos并导入`configs/nacos/`目录下的配置文件
2. **编译打包**
   ```bash
   # 编译所有服务
   mvn clean package -DskipTests

   # 单独编译某个服务（例如：认证服务）
   cd business-services/auth-service
   mvn clean package -DskipTests
   ```
3. **启动服务**
   ```bash
   # 启动API网关
   java -jar gateway/target/gateway-1.0.0.jar

   # 启动认证服务
   java -jar business-services/auth-service/target/auth-service-1.0.0.jar

   # 启动系统服务
   java -jar business-services/system-service/target/system-service-1.0.0.jar

   # 启动文件服务
   java -jar business-services/file-service/target/file-service-1.0.0.jar
   ```

### 前端启动步骤

1. **安装依赖**
   ```bash
   npm install
   ```
2. **启动开发服务器**
   ```bash
   npm run dev
   ```
3. **构建生产版本**
   ```bash
   npm run build
   ```

## 5. API文档

各服务的API文档可通过Knife4j访问：

- API网关: <http://localhost:8080/doc.html>
- 认证服务: <http://localhost:8081/doc.html>
- 系统服务: <http://localhost:8082/doc.html>
- 文件服务: <http://localhost:8083/doc.html>

## 6. 默认登录信息

系统初始化后，默认创建超级管理员账号：

- 用户名: admin
- 密码: 123456
- 角色: 超级管理员（拥有所有权限）

## 7. 部署方式

### 独立部署

#### 后端服务独立部署

各业务服务可独立打包部署：

```bash
# 单独打包认证服务
cd business-services/auth-service
mvn clean package -DskipTests

# 使用Docker部署
cd business-services/auth-service
docker build -t trae-auth-service:1.0.0 .
docker run -d -p 8081:8081 --name auth-service trae-auth-service:1.0.0
```

#### 前端独立部署

```bash
# 构建前端
npm run build

# 使用Nginx部署
docker run -d -p 80:80 -v ./dist:/usr/share/nginx/html --name trae-frontend nginx:alpine
```

### 一键部署

使用docker-compose.yml进行全栈一键部署：

```bash
cd deploy
docker-compose up -d
```

## 8. 新增业务模块步骤

### 1. 创建业务服务模块

1. 在 `business-services` 目录下创建新的业务服务目录，命名格式为 `xxx-service`
2. 创建标准的Spring Boot项目结构
3. 在模块的 `pom.xml` 中添加必要的依赖
4. 创建启动类，添加 `@SpringBootApplication` 注解
5. 配置 `application.yml` 和 `bootstrap.yml` 文件

### 2. 配置Nacos

1. 在 `configs/nacos` 目录下创建新的配置文件 `xxx-service.yaml`
2. 配置服务名称、端口、数据库连接等信息
3. 在Nacos控制台中导入配置文件

### 3. 创建数据库表

1. 在 `docs/sql-scripts` 目录下的SQL脚本中添加新表的创建语句
2. 添加必要的初始化数据

### 4. 实现业务逻辑

1. 创建实体类（Entity）
2. 创建Mapper接口和XML文件
3. 创建Service接口和实现类
4. 创建Controller类
5. 添加必要的权限控制注解

### 5. 配置Docker

1. 在业务服务目录下创建 `Dockerfile` 文件
2. 在 `deploy/docker-compose.yml` 文件中添加新服务的配置

### 6. 集成前端

1. 在前端项目中创建对应的页面组件
2. 实现与后端接口的对接
3. 添加路由配置
4. 实现权限控制

## 9. 基础功能

框架内置完整的基础功能，可直接使用：

- ✅ 登录认证（JWT）：支持用户名密码登录、token刷新
- ✅ 用户管理（增删改查）：支持用户信息管理、状态管理
- ✅ 角色管理（权限分配）：支持角色创建、权限分配、角色菜单关联
- ✅ 菜单管理（动态路由）：支持菜单树管理、权限绑定、动态路由生成
- ✅ 文件管理（上传下载）：支持文件上传、下载、列表查询
- ✅ 多租户支持：基于租户ID的数据隔离
- ✅ 监控告警：集成Spring Boot Actuator和Prometheuse
- ✅ 流量控制：集成Sentinel实现限流熔断

## 10. 故障排除指南

### 1. 服务启动失败

**症状**：服务无法正常启动，日志中显示错误信息

**解决方案**：

- 检查依赖服务（Nacos、Redis、MySQL）是否正常运行
- 检查服务配置是否正确，特别是数据库连接信息
- 检查端口是否被占用
- 查看详细日志信息，定位具体错误原因

### 2. 数据库连接失败

**症状**：服务启动时无法连接到数据库

**解决方案**：

- 检查数据库是否正常运行
- 检查数据库连接字符串是否正确
- 检查数据库用户名和密码是否正确
- 检查网络连接是否正常，防火墙是否开放端口

### 3. Nacos配置问题

**症状**：服务无法从Nacos获取配置，或者配置不生效

**解决方案**：

- 检查Nacos是否正常运行
- 检查服务的 `bootstrap.yml` 配置是否正确
- 检查Nacos配置文件是否存在，格式是否正确
- 检查Nacos命名空间和组配置是否匹配

### 4. 权限认证问题

**症状**：API请求返回401或403错误

**解决方案**：

- 检查JWT令牌是否有效，是否过期
- 检查用户是否有足够的权限访问该API
- 检查权限注解是否正确配置
- 检查请求头中的Authorization是否正确设置

### 5. 前端接口404

**症状**：前端请求后端接口返回404错误

**解决方案**：

- 检查API网关是否正常启动
- 检查网关路由配置是否正确
- 检查后端服务是否正常注册到Nacos
- 检查接口路径是否正确

### 6. 性能问题

**症状**：系统响应缓慢，请求超时

**解决方案**：

- 检查数据库查询是否优化，是否添加了合适的索引
- 检查Redis缓存是否正确使用
- 检查服务是否存在内存泄漏
- 使用Sentinel监控流量，调整限流配置
- 考虑水平扩展服务实例

## 11. 监控与告警

### 1. Spring Boot Actuator

框架集成了Spring Boot Actuator，可以通过以下端点监控系统状态：

```
# 健康检查
http://localhost:8080/actuator/health

# 系统信息
http://localhost:8080/actuator/info

# 指标信息
http://localhost:8080/actuator/metrics

# Prometheus指标
http://localhost:8080/actuator/prometheus
```

### 2. Sentinel流量控制

Sentinel控制台可以监控服务的流量、熔断和限流情况：

```
# Sentinel控制台地址
http://localhost:8080
```

### 3. 日志监控

所有服务的日志都保存在 `logs` 目录下，可以使用ELK等日志收集工具进行集中管理和分析。

### 4. 统一告警

- **Prometheus + Grafana**：监控系统指标和服务状态
- **Sentinel**：流量控制和熔断告警
- **ELK**：日志集中管理和异常告警
- **Nacos**：服务健康状态监控

## 12. 数据库规范

### 1. 分库分表

- 使用MyBatis Plus的分库分表插件，支持按租户ID或业务ID分库
- 配置文件中可灵活配置分库分表策略
- 支持水平分表，自动路由

### 2. 读写分离

- 基于MyBatis Plus实现读写分离
- 主库负责写操作，从库负责读操作
- 支持动态切换数据源

### 3. 审计字段

所有业务表必须包含以下审计字段：

| 字段名 | 类型 | 描述 |
|-------|------|------|
| `create_by` | `bigint` | 创建人ID |
| `create_time` | `datetime` | 创建时间 |
| `update_by` | `bigint` | 更新人ID |
| `update_time` | `datetime` | 更新时间 |
| `tenant_id` | `bigint` | 租户ID |
| `del_flag` | `char(1)` | 删除标志（0-正常，1-删除） |

审计字段由框架自动填充，无需手动设置。

## 13. 多租户隔离

### 1. 隔离机制

- **数据隔离**：基于租户ID的字段隔离
- **配置隔离**：Nacos中不同租户的配置隔离
- **服务隔离**：支持多租户的服务实例隔离

### 2. 实现方式

- 在所有业务表中添加 `tenant_id` 字段
- 使用MyBatis Plus的多租户插件自动过滤数据
- 配置文件中可配置多租户策略

## 14. Spring Security配置

### 1. 认证流程

1. 用户登录，提供用户名和密码
2. 认证服务验证用户名密码
3. 生成JWT令牌并返回
4. 后续请求携带JWT令牌，网关验证令牌有效性
5. 网关将用户信息传递给微服务

### 2. 权限控制

- **基于角色的访问控制（RBAC）**
- **细粒度权限控制**：支持按钮级权限
- **动态权限分配**：权限可实时调整
- **服务间调用权限**：基于服务令牌的认证

### 3. 安全注解

- `@PreAuthorize`：方法级权限控制
- `@RequiresPermissions`：需要特定权限
- `@RequiresRoles`：需要特定角色

## 15. 开发规范

### 1. 代码规范

- 遵循Java命名规范
- 使用Lombok简化代码
- 实现RESTful API设计
- 分层架构（Controller -> Service -> Mapper）

### 2. 日志规范

- 使用SLF4J进行日志记录
- 统一日志格式
- 合理设置日志级别
- 关键操作记录详细日志

### 3. 测试规范

- 编写单元测试和集成测试
- 测试覆盖率不低于80%
- 定期运行测试套件

### 4. 版本规范

- 使用语义化版本控制
- 定期发布版本
- 维护版本变更日志

## 16. 贡献指南

1. Fork项目
2. 创建特性分支
3. 提交代码
4. 推送分支
5. 提交Pull Request

## 17. 许可证

[MIT License](LICENSE)
