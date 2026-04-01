# base-core 公共核心包

## 1. 项目概述

base-core是Trae AI微服务框架的公共核心包，为所有微服务提供通用功能和组件支持。该模块独立封装，可被其他服务依赖和复用，减少重复代码，保证系统一致性。

## 2. 核心功能

- 统一异常处理机制
- 全局响应格式处理
- JWT认证与授权组件
- 权限控制注解
- 分页查询模型
- Feign客户端配置
- 基础安全配置
- 服务间调用token管理

## 3. 目录结构

```
base-core/
├── src/main/java/com/trae/micro/core/
│   ├── config/        # 基础配置类
│   │   └── BaseSecurityConfig.java  # 基础安全配置
│   ├── exception/     # 异常处理
│   │   └── BusinessException.java   # 业务异常类
│   ├── feign/         # Feign客户端配置
│   │   └── ServiceTokenFeignInterceptor.java  # Feign请求拦截器
│   ├── handler/       # 全局处理器
│   │   └── GlobalExceptionHandler.java  # 全局异常处理器
│   ├── model/         # 通用模型
│   │   ├── Menu.java          # 菜单模型
│   │   ├── PageQuery.java     # 分页查询模型
│   │   ├── PageResult.java    # 分页结果模型
│   │   └── R.java             # 统一响应模型
│   └── security/      # 安全组件
│       ├── JwtAuthenticationFilter.java  # JWT认证过滤器
│       ├── JwtUtils.java                  # JWT工具类
│       ├── PermissionAspect.java          # 权限切面
│       ├── RequiresPermission.java        # 权限注解
│       ├── SecurityContextHolder.java     # 安全上下文
│       └── ServiceTokenManager.java       # 服务token管理
└── pom.xml
```

## 4. 核心组件说明

### 4.1 统一响应模型 (R)

`R.java` 定义了统一的API响应格式，包含以下字段：
- `success`: 响应状态（true/false）
- `code`: 响应码
- `message`: 响应消息
- `data`: 响应数据

使用示例：
```java
return R.success(data, "操作成功");
return R.fail("操作失败");
```

### 4.2 JWT认证组件

- `JwtUtils`: 提供JWT token的生成、验证和解析功能
- `JwtAuthenticationFilter`: JWT认证过滤器，用于验证请求中的token
- `SecurityContextHolder`: 安全上下文，用于存储当前登录用户信息

### 4.3 权限控制

- `RequiresPermission`: 权限注解，用于标记需要特定权限的方法
- `PermissionAspect`: 权限切面，用于检查方法调用者是否具有所需权限

使用示例：
```java
@RequiresPermission("user:list")
public R<List<User>> listUsers() {
    // 方法实现
}
```

### 4.4 分页查询

- `PageQuery`: 分页查询参数模型，包含页码、每页条数等
- `PageResult`: 分页查询结果模型，包含数据列表、总条数等

使用示例：
```java
public R<PageResult<User>> list(@ModelAttribute PageQuery pageQuery) {
    PageResult<User> pageResult = userService.page(pageQuery);
    return R.success(pageResult, "查询成功");
}
```

### 4.5 服务间调用token管理

- `ServiceTokenManager`: 用于管理服务间调用的token
- `ServiceTokenFeignInterceptor`: Feign请求拦截器，自动添加服务token到请求头

## 5. 使用方法

### 5.1 依赖引入

在其他服务的pom.xml中添加依赖：

```xml
<dependency>
    <groupId>com.trae.micro</groupId>
    <artifactId>base-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 5.2 继承基础安全配置

在微服务的SecurityConfig中继承BaseSecurityConfig：

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends BaseSecurityConfig {

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        super(jwtAuthenticationFilter);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http = buildBaseSecurityConfig(http);
        // 自定义安全配置
        return http.build();
    }

    @Override
    protected void configureAuthorizeRequests(AuthorizationManagerRequestMatcherRegistry authz) {
        // 配置请求授权规则
        authz.requestMatchers("/api/v1/auth/**").permitAll()
             .anyRequest().authenticated();
    }
}
```

### 5.3 使用统一响应模型

直接使用R类作为Controller方法的返回类型：

```java
@GetMapping
public R<List<User>> list() {
    List<User> users = userService.list();
    return R.success(users, "查询成功");
}
```

### 5.4 使用权限注解

在需要权限控制的方法上添加@RequiresPermission注解：

```java
@RequiresPermission("user:delete")
@DeleteMapping("/{id}")
public R<Void> delete(@PathVariable Long id) {
    userService.removeById(id);
    return R.success(null, "删除成功");
}
```

## 6. 依赖关系

- Spring Boot 3.x
- Spring Security
- jjwt (JWT库)
- Lombok
- Spring Cloud Feign

## 7. 版本更新

### v1.0.0
- 初始版本
- 包含核心功能组件
- 支持JWT认证与授权
- 提供统一异常处理
- 实现权限控制注解

## 8. 贡献指南

1. Fork项目
2. 创建特性分支
3. 提交代码
4. 推送分支
5. 提交Pull Request

## 9. 许可证

[MIT License](LICENSE)
