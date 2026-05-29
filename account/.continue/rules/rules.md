# Account 服务 - 技术架构文档

## 项目概述

`account` 是「地灾资质申请」平台中负责**账号体系管理**的微服务模块，核心管理组织架构、用户、职位、字典等业务实体，提供对应 CRUD 接口。

---

## 1. 总体架构

### 1.1 技术栈

| 层级 | 技术选型 | 说明 |
| ------ | ---------- | ------ |
| **语言/版本** | Java 21 (JDK 21) | 模块化、LTS 版本 |
| **框架** | Spring Boot 3.2.4 + WebFlux | 完全响应式架构 |
| **云原生** | Spring Cloud 2023.0.1 + Alibaba | 服务注册/配置中心 |
| **安全** | Spring Security + OAuth2 Resource Server (JWT) | 资源服务器模式，JWT 鉴权 |
| **数据库** | MySQL (通过 R2DBC) | 异步非阻塞反应式驱动 |
| **数据库迁移** | Flyway | 版本化管理 DDL |
| **ID 生成** | 雪花算法 (Hutool Snowflake) | 全局唯一分布式 ID |
| **配置中心** | Nacos (Config + Discovery) | 外部化配置 + 服务发现 |
| **对象存储** | MinIO | 文件/头像存储 |
| **构建工具** | Maven (多模块) | 统一依赖管理 |

### 1.2 项目模块结构

di-zai-zi-zhi-shen-qing (父 POM)
├── account/                    ← 本服务（账号中心）
│   ├── src/main/java/.../account/
│   │   ├── AccountApp.java          # 启动类
│   │   ├── config/                  # 安全、JWT、雪花算法配置
│   │   ├── controller/              # REST 控制器（WebFlux）
│   │   ├── service/                 # 业务接口 + 实现
│   │   ├── repository/              # R2DBC 数据访问层
│   │   ├── entity/                  # 数据实体（ORM 映射）
│   │   ├── param/                   # 请求参数 DTO
│   │   ├── vo/                      # 响应视图对象
│   │   └── utils/                   # 工具类（密码工具）
│   └── src/main/resources/
│       ├── application.yml          # 本地配置（Nacos 地址）
│       └── nacos-account.yml        # Nacos 托管配置
├── common/                    ← 公共模块（被所有模块依赖）
│   └── src/main/java/.../common/
│       ├── entity/CommonEntity.java         # 实体基类（ID、审计字段）
│       ├── repository/CommonReactiveCrudRepository # 基础 Repository
│       ├── service/CommonService.java       # 通用 CRUD 接口
│       ├── service/impl/CommonServiceImpl   # 通用 CRUD 实现
│       ├── config/ReactiveSecurityContext.java  # 安全上下文工具
│       ├── CommonSnowflake.java             # 雪花 ID 封装
│       └── commonminio/                     # MinIO 文件上传
├── auth-center/               ← 认证中心（OAuth2 授权服务器）
├── gateway/                   ← API 网关（路由转发）
├── certification-catalog/     ← 认证目录服务
├── rolemanage/                ← 权限管理服务
└── file-manage/               ← 文件管理服务

---

## 2. 核心架构分层

### 2.1 数据访问层（Repository）

- 继承通用基接口 `CommonReactiveCrudRepository<T extends CommonEntity>`
- 底层基于 **Spring Data R2DBC**（非 MyBatis/JPA）
- 返回 `Mono<T>` / `Flux<T>` 响应式流
- 自定义复杂查询使用 `@Query` 注解直接写 SQL

**关键约定：**

- 逻辑删除：所有表使用 `delete_flag = 'NOT_DELETED' / 'DELETED'`
- 分页：使用 `Pageable`（先转成 `PageRequest`）
- 不支持懒加载、级联等 ORM 特性，需手动编写 SQL

### 2.2 业务逻辑层（Service）

- 接口继承 `CommonService<T>`，提供 `saveOrUpdate`、`findById` 等通用方法
- 实现类继承 `CommonServiceImpl<T>`，自动注入 `CommonSnowflake` 与 `ReactiveSecurityContext`
- **`saveOrUpdate` 核心逻辑：**
  - `id == null`：生成雪花 ID → 设置为 new → 写入 `createTime` / `createUser`（从 JWT 的 `user_id` claim 获取）
  - `id != null`：设置为 not new → 写入 `updateTime` / `updateUser`
- 所有业务方法返回 `Mono` / `Flux`，保持响应式链条

### 2.3 表示层（Controller）

- 使用 `@RestController` + `@RequestMapping` 定义 REST API
- 所有方法返回 `ResponseEntity<Mono<T>>` / `ResponseEntity<Flux<T>>`
- 请求参数验证：`@Valid` + `jakarta.validation` 注解
- 统一异常处理：`@RestControllerAdvice` + `ValidationExceptionHandler`

### 2.4 配置层（Config）

| 配置类 | 职责 |
| -------- | ------ |
| `SecurityConfig` | 构建 `SecurityWebFilterChain`，禁用表单/csrf，启用 OAuth2 JWT 资源服务器 |
| `CustomJwtAuthenticationConverter` | **从 JWT 解析 scope/roles 权限**，注入 `SimpleGrantedAuthority` |
| `UserContextWebFilter` | 从 Gateway 传递的请求头 `X-User-Id` / `X-User-Name` 提取用户信息，写入 Reactor Context |
| `ReactiveUserContext` | Reactor Context 辅助工具：`getUserId()` / `getUserName()` |
| `SnowflakeConfig` | 读取配置 `snowflake.worker-id` / `datacenter-id` / `start-timestamp`，注入 `CommonSnowflake` Bean |
| `ValidationExceptionHandler` | 全局异常处理：校验失败、JSON 解析错误等 |

---

## 3. 实体模型（Entity）

所有实体继承 `CommonEntity`（包路径 `org.ziranziyuanting.common.entity`），其包含：

```java
@Id
private Long id;               // 雪花 ID
private String deleteFlag;     // "NOT_DELETED" / "DELETED"
private LocalDateTime createTime;
private Long createUser;
private LocalDateTime updateTime;
private Long updateUser;
private boolean isNew = true;  // R2DBC 新增/更新标识
```

| 实体 | 表名 | 关键字段 |
| ------ | ------ | ---------- |
| `SysOrg` | `sys_org` | parentId, name, code, category, sortCode, directorId |
| `SysUser` | `sys_user` | account, password, name, orgId, positionId, phone, email, gender 等 40+ 字段 |
| `SysDict` | `sys_dict` | parentId, dictLabel, dictValue, category, sortCode |
| `SysPosition` | `sys_position` | 职位实体 |
| `YlTest` | `yl_test` | 测试表 |
| `RegisteredClientEntity` | `registered_client_entity` | OAuth2 客户端实体 |

---

## 4. 安全架构

### 4.1 认证流程

[用户/前端] → [Gateway] → (JWT Token) → [Account Service]
                                              │
                                    ┌─────────┴──────────┐
                                    │ Spring Security      │
                                    │ OAuth2 Resource Server│
                                    └─────────┬──────────┘
                                              │
                                    ┌─────────┴──────────┐
                                    │ JWT Validator        │
                                    │ (issuer-uri: auth-center:20001) │
                                    └─────────┬──────────┘
                                              │
                                    ┌─────────┴──────────┐
                                    │ CustomJwtAuthConverter│
                                    │ 提取 scope + roles   │
                                    └─────────┬──────────┘
                                              │
                                    ┌─────────┴──────────┐
                                    │ Gateway 转发       │
                                    │ X-User-Id / X-User-Name │
                                    └─────────────────────┘

### 4.2 关键安全配置

- **所有接口默认需要认证**（`.anyExchange().authenticated()`）
- JWT 的 `issuer-uri` 指向 `auth-center` 服务（`:20001`）
- **用户身份传播方式**：
  - Gateway 从 JWT 解析出 `user_id`，通过请求头 `X-User-Id` / `X-User-Name` 传递
  - `UserContextWebFilter` 读取请求头 → 写入 Reactor `Context`
  - `ReactiveUserContext` 提供静态方法读取
  - `ReactiveSecurityContext`（common 模块）直接从 SecurityContext 的 JWT 中提取 `user_id` claim

---

## 5. 分布式 ID 方案

采用 **雪花算法（Snowflake）**：

ID = CommonSnowflake.nextId()

- 封装 `cn.hutool.core.lang.Snowflake`
- 配置在 Nacos 中：`snowflake.worker-id` / `snowflake.datacenter-id` / `start-timestamp`
- 在 `CommonServiceImpl.saveOrUpdate()` 中自动注入，上层 Service 无需关心 ID 生成

---

## 6. 数据库 & 迁移

### 6.1 技术选型

| 组件 | 用途 |
| ------ | ------ |
| **R2DBC MySQL** (`io.asyncer:r2dbc-mysql`) | 运行时异步数据库访问 |
| **Flyway** | DDL 版本化迁移 |
| **MySQL JDBC** (`mysql-connector-j`) | 仅用于 Flyway 迁移（非运行时） |

### 6.2 Flyway 迁移脚本

resources/db/migration/
├── V1__Create_yl_test_table.sql
├── V2__Create_sys_org_table.sql
├── V2.1__Alter_sys_org_table.sql
├── V3__Create_dev_dict_table.sql
├── V4__Create_sys_position_table.sql
├── V5__Create_sys_user_table.sql
├── V5.1__Alter_sys_user_table.sql
├── V5.2__Alter_sys_user_table.sql
├── V6__Create_registered_client_entity_table.sql
└── V6.1__Alter_registered_client_entity_table.sql

---

## 7. 配置中心（Nacos）

### 配置文件层次

application.yml                      # 本地：声明 Nacos 地址 + config import
  └─> nacos://account.yml?group=DI_ZAI_ZI_ZHI_SHEN_QING
                                     # Nacos 远程配置：数据库、端口、日志、雪花算法

**Nacos 托管配置 (`nacos-account.yml`) 包含：**

- `server.port: 20006`
- `spring.security.oauth2.resourceserver.jwt.issuer-uri`
- `spring.r2dbc.*` 数据库连接
- `spring.flyway.*` 迁移配置
- `snowflake.*` 雪花算法参数
- 日志级别设置

### 环境变量注入

| 环境变量 | 说明 | 默认值 |
|----------|------|--------|
| `NACOS_HOST` | Nacos 服务器地址 | `192.168.1.249` |
| `DB_HOST` | 数据库主机 | `localhost` |
| `DB_PORT` | 数据库端口 | `13306` |
| `DB_NAME` | 数据库名 | `auth_center` |
| `DB_USER` | 数据库用户 | `auth_center` |
| `DB_PASSWORD` | 数据库密码 | `pass` |
| `AUTH_CENTER_HOST` | 认证中心地址 | - |

---

## 8. 通用模块（common）

`common` 是一个公共 JAR 包（`org.ziranziyuanting:common`），提供：

| 组件 | 描述 |
|------|------|
| `CommonEntity` | 实体基类（ID、审计字段、逻辑删除） |
| `CommonReactiveCrudRepository<T>` | Repository 基接口 |
| `CommonService<T>` / `CommonServiceImpl<T>` | 通用 CRUD 封装（自动 ID 生成 + 审计） |
| `CommonSnowflake` | 雪花算法封装 |
| `ReactiveSecurityContext` | 从 SecurityContext 提取当前用户 ID |
| `MinioService` | 文件上传封装 |
| `FileUploadService` / `FileDownloadController` | 文件服务接口 |

---

## 9. API 概览

| 控制器 | 路径 | 主要接口 |
|--------|------|----------|
| `SysOrgController` | `/org` | `all`, `add`, `update`, `delete`, `orgTree`, `page` |
| `SysUserController` | `/sysUser` | `add`, `updatePassword`, `getCurrentUser` |
| `SysDictController` | `/dict` | `findAll`, `add` |
| `SysPositionController` | `/position` | 职位管理 |
| `UserController` | `/user` | `regist`（预留）, `all`, `test` |
| `MyTestController` | - | 测试接口 |

---

## 10. 关键设计原则

1. **全响应式**：从 Controller 到 Repository 全程使用 WebFlux + R2DBC Reactor 链，禁止阻塞调用。
2. **JWT 传播**：用户信息通过 SecurityContext + Reactor Context 双重方式向下传递。
3. **逻辑删除**：所有业务表不物理删除，使用 `delete_flag` 软删除。
4. **外部化配置**：所有环境相关参数通过 Nacos 或环境变量管理，不硬编码。
5. **统一审计**：创建/更新时间、人员由框架 `CommonServiceImpl` 自动填充。
6. **职责单一**：Account 只负责账号体系（组织/用户/字典），认证由 `auth-center` 负责。