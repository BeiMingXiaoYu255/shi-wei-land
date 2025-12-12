# 基于 JSP+SpringBoot+MySQL 的微软 C# 教程风格网页开发解决方案

## 一、方案概述

本方案旨在构建一套符合微软 C# 教程页面风格的网页系统，采用 MVC 架构模式，整合 JSP（视图层）、SpringBoot（控制层 / 服务层）、MySQL（数据层）技术栈，覆盖实体设计、数据库设计、项目结构、版本控制、加密、工具类、页面、日志、防内存溢出等全维度设计要求，确保系统具备可维护性、安全性、稳定性。

## 二、核心技术栈与依赖清单

### 1. 基础开发环境

| 工具 / 环境  | 版本要求                            | 说明                                   |
| ------------ | ----------------------------------- | -------------------------------------- |
| JDK          | 17（LTS）                           | 推荐长期支持版本，兼容 SpringBoot 3.x  |
| Maven/Gradle | Maven 3.8+ / Gradle 7.5+            | 项目构建工具，优先 Maven（生态更成熟） |
| IDE          | IntelliJ IDEA 2023+ / Eclipse 2023+ | 开发工具，IDEA 更适配 SpringBoot       |
| MySQL        | 8.0+                                | 数据库，支持 JSON 类型、索引优化       |
| Tomcat       | 9.0+/10.0+                          | SpringBoot 内嵌 Tomcat，无需单独部署   |
| Git          | 2.40+                               | 版本控制工具                           |
| JSP          | 2.3+                                | 视图层技术，需整合 SpringBoot 支持     |

### 2. Maven 核心依赖（pom.xml）

| 依赖名称                       | 版本    | 作用                             |
| ------------------------------ | ------- | -------------------------------- |
| spring-boot-starter-web        | 3.2.x   | 核心 Web 依赖（含 Spring MVC）   |
| spring-boot-starter-jdbc       | 3.2.x   | JDBC 连接支持                    |
| spring-boot-starter-data-jpa   | 3.2.x   | 数据持久层（可选，替代 MyBatis） |
| mybatis-spring-boot-starter    | 3.0.3   | MyBatis 整合（推荐，灵活度高）   |
| mysql-connector-java           | 8.0.33  | MySQL 驱动                       |
| javax.servlet.jsp.jstl         | 1.2     | JSTL 标签库，JSP 数据渲染        |
| org.apache.tomcat.embed        | 9.0.80  | Tomcat JSP 支持                  |
| spring-boot-starter-security   | 3.2.x   | 安全框架（加密、权限控制）       |
| bcrypt                         | 0.4     | BCrypt 加密算法依赖              |
| commons-codec                  | 1.15    | 通用加密工具（MD5/SHA 等）       |
| spring-boot-starter-logging    | 3.2.x   | 日志核心依赖                     |
| logback-classic                | 1.4.11  | 日志实现（SLF4J 适配）           |
| lombok                         | 1.18.30 | 简化实体类代码（注解式）         |
| spring-boot-starter-validation | 3.2.x   | 参数校验（防非法数据）           |
| commons-lang3                  | 3.14.0  | 通用工具类（字符串、集合等）     |
| fastjson2                      | 2.0.32  | JSON 序列化 / 反序列化           |
| spring-boot-starter-aop        | 3.2.x   | AOP 切面（日志、性能监控）       |
| com.github.ben-manes.caffeine  | 3.1.8   | 缓存（减少数据库压力，防 OOM）   |
| spring-boot-starter-actuator   | 3.2.x   | 应用监控（内存、线程监控）       |

## 三、MVC 架构分层设计

### 1. 架构总览

遵循 MVC 设计模式，将系统分为**模型（Model）**、**视图（View）**、**控制器（Controller）** 三层，同时补充服务层（Service）、数据访问层（DAO/Mapper）、工具层（Util）等，各层职责清晰、低耦合。

| 层级                   | 技术实现              | 核心职责                                                     |
| ---------------------- | --------------------- | ------------------------------------------------------------ |
| 视图层（View）         | JSP + JSTL + CSS/JS   | 展示教程内容、交互界面；接收用户输入，转发至控制器；支持响应式布局，适配微软 C# 教程页面风格。 |
| 控制器层（Controller） | SpringBoot Controller | 接收前端请求（GET/POST），参数校验，调用服务层；返回数据 / 跳转页面；统一异常处理。 |
| 服务层（Service）      | SpringBoot Service    | 业务逻辑核心层；整合多个 DAO 操作，实现事务控制；封装业务规则（如教程分类、权限校验）。 |
| 数据访问层（DAO）      | MyBatis Mapper/ JPA   | 数据库 CRUD 操作；SQL 映射（MyBatis）或 ORM 映射（JPA）；参数绑定，防止 SQL 注入。 |
| 模型层（Model）        | 实体类（Entity）+ DTO | 实体类映射数据库表；DTO 封装前端交互数据（减少冗余字段）；VO 封装视图展示数据。 |

### 2. 各层交互流程

```plaintext
用户请求 → JSP（View） → Controller（参数校验） → Service（业务逻辑） → DAO（数据库操作）
                                                                 ↓
数据库返回数据 → DAO → Service → Controller → JSP（View渲染） → 展示给用户
```

## 四、实体类设计

结合微软 C# 教程页面的核心内容（教程分类、章节、内容、作者、评论、用户权限等），设计核心实体类，遵循 JavaBean 规范，使用 Lombok 简化代码。

### 1. 核心实体类清单

| 实体类名称       | 对应数据库表      | 核心字段                                                     | 说明                                       |
| ---------------- | ----------------- | ------------------------------------------------------------ | ------------------------------------------ |
| User             | sys_user          | id（主键）、username（用户名）、password（加密密码）、email、role（角色：admin/visitor）、createTime、updateTime | 系统用户（管理员 / 访客）                  |
| TutorialCategory | tutorial_category | id、name（分类名：如基础语法、面向对象）、description、sort（排序）、createTime | 教程分类（对应微软 C# 教程的大分类）       |
| TutorialChapter  | tutorial_chapter  | id、categoryId（关联分类）、title（章节标题）、content（章节简介）、sort、status（是否发布）、createTime | 教程章节（分类下的子章节）                 |
| TutorialContent  | tutorial_content  | id、chapterId（关联章节）、title、content（富文本内容）、authorId（关联用户）、viewCount（浏览量）、updateTime | 教程具体内容（核心，对应微软教程的详情页） |
| Comment          | tutorial_comment  | id、contentId（关联教程内容）、userId（关联用户）、content（评论内容）、createTime、status（审核状态） | 教程评论                                   |
| OperationLog     | sys_operation_log | id、userId、module（操作模块）、operation（操作类型：新增 / 修改 / 删除）、ip、operTime、content | 操作日志                                   |

### 2. 实体类设计规范

- 主键统一使用`Long`类型，自增策略（MySQL AUTO_INCREMENT）；
- 通用字段（createTime、updateTime、status）抽离为`BaseEntity`基类，其他实体类继承；
- 敏感字段（如 password）不参与序列化（`@JsonIgnore`）；
- 关联字段使用外键映射（如 categoryId 关联 TutorialCategory 的 id），Service 层处理关联查询；
- 使用`@Data`（Lombok）简化 get/set，`@TableName`（MyBatis-Plus）映射表名，`@Column`映射字段名。

## 五、数据库表设计

### 1. 数据库基础配置

- 字符集：UTF8MB4（支持 emoji、特殊字符）；
- 排序规则：utf8mb4_general_ci；
- 存储引擎：InnoDB（支持事务、外键、行级锁）；
- 主键：自增 INT/BIGINT，非主键字段建立合适索引（如分类 ID、章节 ID）。

### 2. 核心表结构设计

#### （1）系统用户表（sys_user）

| 字段名      | 类型         | 约束                          | 说明                         |
| ----------- | ------------ | ----------------------------- | ---------------------------- |
| id          | BIGINT       | PRIMARY KEY                   | 主键，自增                   |
| username    | VARCHAR(50)  | NOT NULL UNIQUE               | 用户名，唯一                 |
| password    | VARCHAR(100) | NOT NULL                      | 加密后的密码（BCrypt）       |
| email       | VARCHAR(100) | UNIQUE                        | 邮箱，用于登录 / 找回密码    |
| role        | VARCHAR(20)  | DEFAULT 'visitor'             | 角色：admin/visitor          |
| create_time | DATETIME     | DEFAULT NOW()                 | 创建时间                     |
| update_time | DATETIME     | DEFAULT NOW() ON UPDATE NOW() | 更新时间                     |
| is_delete   | TINYINT      | DEFAULT 0                     | 逻辑删除：0 - 未删，1 - 已删 |

#### （2）教程分类表（tutorial_category）

| 字段名      | 类型         | 约束          | 说明             |
| ----------- | ------------ | ------------- | ---------------- |
| id          | BIGINT       | PRIMARY KEY   | 主键，自增       |
| name        | VARCHAR(100) | NOT NULL      | 分类名称         |
| description | TEXT         |               | 分类描述         |
| sort        | INT          | DEFAULT 0     | 排序权重（升序） |
| create_time | DATETIME     | DEFAULT NOW() | 创建时间         |
| is_delete   | TINYINT      | DEFAULT 0     | 逻辑删除         |

#### （3）教程章节表（tutorial_chapter）

| 字段名      | 类型         | 约束                          | 说明                     |
| ----------- | ------------ | ----------------------------- | ------------------------ |
| id          | BIGINT       | PRIMARY KEY                   | 主键，自增               |
| category_id | BIGINT       | NOT NULL                      | 关联分类表主键（外键）   |
| title       | VARCHAR(200) | NOT NULL                      | 章节标题                 |
| content     | TEXT         |                               | 章节简介                 |
| sort        | INT          | DEFAULT 0                     | 排序权重                 |
| status      | TINYINT      | DEFAULT 1                     | 状态：1 - 发布，0 - 草稿 |
| create_time | DATETIME     | DEFAULT NOW()                 | 创建时间                 |
| update_time | DATETIME     | DEFAULT NOW() ON UPDATE NOW() | 更新时间                 |
| is_delete   | TINYINT      | DEFAULT 0                     | 逻辑删除                 |

#### （4）教程内容表（tutorial_content）

| 字段名      | 类型         | 约束                          | 说明                   |
| ----------- | ------------ | ----------------------------- | ---------------------- |
| id          | BIGINT       | PRIMARY KEY                   | 主键，自增             |
| chapter_id  | BIGINT       | NOT NULL                      | 关联章节表主键（外键） |
| title       | VARCHAR(200) | NOT NULL                      | 内容标题               |
| content     | LONGTEXT     | NOT NULL                      | 教程详情（富文本）     |
| author_id   | BIGINT       | NOT NULL                      | 关联用户表主键（作者） |
| view_count  | INT          | DEFAULT 0                     | 浏览量                 |
| create_time | DATETIME     | DEFAULT NOW()                 | 创建时间               |
| update_time | DATETIME     | DEFAULT NOW() ON UPDATE NOW() | 更新时间               |
| is_delete   | TINYINT      | DEFAULT 0                     | 逻辑删除               |

#### （5）其他表（评论、操作日志）

- 评论表（tutorial_comment）：补充外键索引（content_id、user_id），添加索引提升查询效率；
- 操作日志表（sys_operation_log）：ip 字段建立普通索引，module 字段建立普通索引；
- 所有表添加`is_delete`字段实现逻辑删除，避免物理删除导致数据丢失。

### 3. 索引设计

- 主键索引：所有表的 id 字段；
- 外键索引：chapter_id、category_id、author_id、user_id 等关联字段；
- 普通索引：view_count（热门教程排序）、create_time（最新教程排序）、username（用户登录）；
- 联合索引：(module, operation)（日志查询）、(category_id, sort)（分类下章节排序）。

## 六、项目结构设计

采用 SpringBoot 标准项目结构，结合 MVC 分层，目录清晰、便于扩展。

```plaintext
com.csharp.tutorial
├── config/                # 配置层
│   ├── MyBatisConfig.java # MyBatis配置（分页、别名）
│   ├── SecurityConfig.java # 安全配置（加密、权限）
│   ├── WebConfig.java     # Web配置（JSP视图解析、拦截器）
│   └── LogConfig.java     # 日志配置
├── controller/            # 控制器层
│   ├── UserController.java # 用户相关接口（登录、注册）
│   ├── TutorialCategoryController.java # 分类接口
│   ├── TutorialChapterController.java # 章节接口
│   ├── TutorialContentController.java # 内容接口
│   └── CommentController.java # 评论接口
├── service/               # 服务层
│   ├── impl/              # 服务实现类
│   │   ├── UserServiceImpl.java
│   │   ├── TutorialCategoryServiceImpl.java
│   │   └── ...
│   ├── UserService.java
│   ├── TutorialCategoryService.java
│   └── ...
├── mapper/                # 数据访问层（MyBatis）
│   ├── UserMapper.java
│   ├── TutorialCategoryMapper.java
│   └── ...
├── model/                 # 模型层
│   ├── entity/            # 实体类（映射数据库）
│   │   ├── BaseEntity.java
│   │   ├── User.java
│   │   └── ...
│   ├── dto/               # 数据传输对象（前端交互）
│   │   ├── UserLoginDTO.java
│   │   ├── TutorialContentDTO.java
│   │   └── ...
│   └── vo/                # 视图对象（页面展示）
│       ├── TutorialContentVO.java
│       └── ...
├── util/                  # 工具类层
│   ├── EncryptUtil.java   # 加密工具
│   ├── LogUtil.java       # 日志工具
│   ├── ValidateUtil.java  # 校验工具
│   ├── DateUtil.java      # 日期工具
│   └── MemoryMonitorUtil.java # 内存监控工具
├── exception/             # 异常处理
│   ├── GlobalExceptionHandler.java # 全局异常处理器
│   ├── BusinessException.java # 业务异常
│   └── SystemException.java # 系统异常
├── interceptor/           # 拦截器
│   ├── LoginInterceptor.java # 登录拦截
│   ├── OperationLogInterceptor.java # 操作日志拦截
│   └── MemoryInterceptor.java # 内存监控拦截
├── resources/             # 资源文件
│   ├── mybatis/           # MyBatis映射文件
│   │   ├── UserMapper.xml
│   │   └── ...
│   ├── static/            # 静态资源
│   │   ├── css/           # 样式（复刻微软C#教程风格）
│   │   ├── js/            # 脚本
│   │   └── images/        # 图片
│   ├── templates/         # JSP视图文件
│   │   ├── index.jsp      # 首页（微软C#教程风格）
│   │   ├── category.jsp   # 分类页
│   │   ├── content.jsp    # 教程详情页
│   │   └── login.jsp      # 登录页
│   ├── application.yml    # 核心配置（数据库、端口、视图解析）
│   ├── application-dev.yml # 开发环境配置
│   ├── application-prod.yml # 生产环境配置
│   └── logback.xml        # 日志配置
└── CsharpTutorialApplication.java # 启动类
```

## 七、版本控制设计

基于 Git 实现版本控制，遵循 Git Flow 分支管理规范，确保开发、测试、生产环境代码隔离。

### 1. 分支策略

| 分支名称      | 用途                                                         | 合并规则                                                     |
| ------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| master/main   | 生产环境分支，存放稳定可发布代码                             | 仅从 release 分支合并，禁止直接提交                          |
| develop       | 开发主分支，整合各功能分支代码                               | 从 feature 分支合并，测试通过后合并到 release 分支           |
| feature/xxx   | 功能分支，如 feature/category-manage（分类管理）、feature/comment（评论功能） | 从 develop 分支创建，开发完成后合并回 develop 分支，命名规范：feature / 功能名 |
| release/x.x.x | 发布分支，如 release/1.0.0（1.0.0 版本发布）                 | 从 develop 分支创建，测试修复 bug 后，合并到 master 和 develop 分支 |
| hotfix/xxx    | 紧急修复分支，如 hotfix/login-error（登录 bug 修复）         | 从 master 分支创建，修复完成后合并到 master 和 develop 分支  |

### 2. 提交规范

采用 Conventional Commits 规范，提交信息格式：`类型(范围): 描述`

- 类型：feat（新功能）、fix（修复 bug）、docs（文档）、style（格式）、refactor（重构）、test（测试）、chore（构建）；
- 示例：`feat(category): 新增教程分类排序功能`、`fix(content): 修复教程内容富文本渲染异常`。

### 3. 版本号规范

遵循语义化版本（Semantic Versioning）：`主版本号.次版本号.修订号`

- 主版本号（MAJOR）：不兼容的 API 变更（如数据库结构大改）；
- 次版本号（MINOR）：向后兼容的功能新增（如新增评论功能）；
- 修订号（PATCH）：向后兼容的问题修复（如 bug 修复）；
- 示例：1.0.0（初始版本）、1.1.0（新增分类功能）、1.1.1（修复分类排序 bug）。

### 4. 协作流程

1. 开发者从 develop 分支创建 feature 分支；
2. 本地开发完成后，提交代码并推送至远程 feature 分支；
3. 创建 Merge Request（MR）/Pull Request（PR），指定审核人；
4. 审核通过后，合并到 develop 分支，删除 feature 分支；
5. 测试完成后，从 develop 分支创建 release 分支，发布前测试；
6. 发布完成后，合并 release 分支到 master 和 develop 分支；
7. 生产环境 bug 通过 hotfix 分支修复，合并到 master 和 develop。

## 八、加密设计

覆盖用户密码、接口传输、数据存储等全链路加密，确保系统安全性。

### 1. 密码加密

- 算法：BCrypt（不可逆加密，自带盐值，抗彩虹表攻击）；
- 实现：用户注册 / 修改密码时，通过`BCryptPasswordEncoder`加密后存储到数据库；登录时，将用户输入密码加密后与数据库密文比对；
- 配置：在`SecurityConfig`中配置`BCryptPasswordEncoder`为默认密码编码器。

### 2. 接口传输加密

- 敏感接口（如登录、用户信息修改）：采用 HTTPS 协议（配置 SSL 证书），防止数据明文传输；
- 接口参数签名：核心接口（如教程内容修改）添加签名验证，规则：`签名 = MD5(参数拼接+时间戳+密钥)`，服务端校验签名和时间戳（防重放攻击）；
- 防 XSS：前端输入过滤（JS 过滤特殊字符）+ 后端参数校验（`@Valid` + 自定义 XSS 过滤器），防止跨站脚本攻击。

### 3. 数据存储加密

- 敏感字段加密：如用户邮箱、手机号，采用 AES 对称加密（密钥存储在配置中心，非代码硬编码）；
- 防 SQL 注入：MyBatis 使用参数绑定（`#{}`），避免拼接 SQL；服务层添加参数校验（如长度、格式）。

### 4. 权限控制加密

- Token 认证：用户登录后生成 JWT Token（包含用户 ID、角色，设置过期时间），Token 签名采用 RSA 非对称加密；
- Token 传输：Token 放在 HTTP 请求头（Authorization: Bearer {token}），避免 Cookie 劫持；
- 权限校验：通过 Spring Security 的`@PreAuthorize`注解控制接口权限（如`@PreAuthorize("hasRole('ADMIN')")`）。

## 九、工具类设计

封装通用工具类，提高代码复用性，降低耦合，涵盖加密、日志、校验、日期、内存监控等核心场景。

### 1. 核心工具类清单

| 工具类名称        | 核心功能                                                     | 技术依赖                         |
| ----------------- | ------------------------------------------------------------ | -------------------------------- |
| EncryptUtil       | 1. BCrypt 密码加密 / 校验；2. AES 对称加密 / 解密；3. MD5/SHA256 哈希；4. JWT Token 生成 / 解析 | bcrypt、commons-codec、jjwt      |
| LogUtil           | 1. 统一日志格式；2. 操作日志记录（AOP 切面）；3. 异常日志封装 | logback、spring-aop              |
| ValidateUtil      | 1. 参数非空校验；2. 格式校验（邮箱、手机号、URL）；3. 分页参数校验 | spring-validation、commons-lang3 |
| DateUtil          | 1. 日期格式化（LocalDateTime/Date 互转）；2. 时间差计算；3. 时区转换 | java.time 包                     |
| HttpUtil          | 1. HTTP 请求发送（GET/POST）；2. 响应结果解析；3. Cookie/Header 处理 | spring-web                       |
| MemoryMonitorUtil | 1. JVM 内存监控（堆 / 非堆内存）；2. 内存阈值告警；3. 线程池状态监控 | java.lang.management             |
| FileUtil          | 1. 富文本图片上传 / 下载；2. 文件大小校验；3. 临时文件清理   | commons-io                       |
| CacheUtil         | 1. 本地缓存（Caffeine）操作；2. 缓存过期策略；3. 缓存命中率统计 | caffeine                         |

### 2. 工具类设计规范

- 所有工具类采用静态方法，私有化构造方法（防止实例化）；
- 异常统一捕获，封装为业务异常（`BusinessException`），便于全局处理；
- 配置项（如加密密钥、缓存过期时间）从`application.yml`读取，避免硬编码；
- 工具类添加详细注释，说明参数、返回值、异常场景。

## 十、页面设计

复刻微软 C# 教程页面的视觉风格和交互逻辑，基于 JSP+CSS/JS 实现，兼顾美观性和实用性。

### 1. 页面结构

| 页面名称                | 核心功能                                             | 设计要点                                                     |
| ----------------------- | ---------------------------------------------------- | ------------------------------------------------------------ |
| 首页（index.jsp）       | 展示教程分类导航、热门教程、最新教程、推荐内容       | 1. 顶部导航栏（复刻微软风格，含搜索框）；2. 左侧分类栏；3. 右侧热门 / 最新推荐；4. 响应式布局（PC / 平板 / 手机） |
| 分类页（category.jsp）  | 展示指定分类下的所有章节，支持排序、筛选             | 1. 分类面包屑；2. 章节列表（标题 + 简介 + 发布时间）；3. 分页控件；4. 筛选按钮（发布状态 / 排序） |
| 详情页（content.jsp）   | 展示教程具体内容，支持评论、收藏、点赞               | 1. 富文本内容渲染（兼容微软教程的代码高亮、表格、图片）；2. 评论区（分页 + 审核状态）；3. 目录导航（章节跳转） |
| 登录页（login.jsp）     | 用户登录 / 注册，密码找回                            | 1. 微软风格表单设计；2. 验证码；3. 密码加密传输；4. 错误提示（非明文） |
| 管理后台（admin/*.jsp） | 教程分类 / 章节 / 内容的增删改查，用户管理，日志查看 | 1. 侧边栏权限导航；2. 表单校验；3. 操作日志实时展示；4. 批量操作功能 |

### 2. 视觉与交互设计

- 风格复刻：参考微软 C# 教程页面的配色（主色：蓝色系）、字体（Segoe UI / 微软雅黑）、布局（左侧导航 + 右侧内容）；
- 代码高亮：集成 Prism.js/Highlight.js，实现 C# 代码语法高亮，与微软教程一致；
- 交互优化：1. 目录锚点跳转；2. 内容滚动时导航栏固定；3. 评论实时加载（无刷新）；4. 搜索联想提示；
- 性能优化：1. 静态资源（CSS/JS/ 图片）压缩；2. 图片懒加载；3. 页面缓存（避免重复渲染）。

### 3. JSP 配置

在`WebConfig`中配置 JSP 视图解析器：

```yaml
spring:
  mvc:
    view:
      prefix: /WEB-INF/templates/ # JSP文件存放路径
      suffix: .jsp # 后缀
  web:
    resources:
      static-locations: classpath:/static/ # 静态资源路径
```

## 十一、日志设计

采用 SLF4J+Logback 实现日志管理，覆盖系统运行全流程，便于问题排查和系统监控。

### 1. 日志级别与输出规则

| 日志级别 | 适用场景                                                     | 输出目标                                  |
| -------- | ------------------------------------------------------------ | ----------------------------------------- |
| ERROR    | 系统异常（如数据库连接失败、接口调用异常）、业务错误（如参数校验失败） | 日志文件（按天分割）+ 告警（邮件 / 钉钉） |
| WARN     | 非致命问题（如缓存过期、接口响应超时）、潜在风险（如内存使用率达 80%） | 日志文件                                  |
| INFO     | 核心业务操作（如用户登录、教程发布）、系统启动 / 停止、接口调用成功 | 日志文件 + 控制台（开发环境）             |
| DEBUG    | 开发调试（如 SQL 语句、参数值、方法调用栈）                  | 控制台（仅开发环境）                      |
| TRACE    | 详细调试（如底层框架调用）                                   | 关闭（生产环境）                          |

### 2. 日志存储设计

- 存储路径：生产环境`/var/log/csharp-tutorial/`，开发环境`./logs/`；
- 分割规则：按天分割（如`csharp-tutorial-2025-12-10.log`），保留 30 天日志；
- 日志格式：`[%d{yyyy-MM-dd HH:mm:ss}] [%thread] [%level] [%logger{50}] - %msg%n`；
- 归档策略：超过 30 天的日志压缩归档，超过 3 个月的日志自动清理。

### 3. 日志分类

- 系统日志：记录系统启动、配置加载、异常等（logger 名称：`com.csharp.tutorial`）；
- 操作日志：记录用户操作（如新增教程、修改评论），通过 AOP 切面统一拦截（logger 名称：`com.csharp.tutorial.operation`）；
- 访问日志：记录 HTTP 请求（如 URL、IP、响应时间），通过 Filter 拦截（logger 名称：`com.csharp.tutorial.access`）。

### 4. 日志监控

- 集成 ELK（Elasticsearch+Logstash+Kibana）：收集日志并可视化展示，支持日志检索、异常告警；
- 关键异常告警：ERROR 级别日志触发邮件 / 钉钉告警，包含异常堆栈、发生时间、服务器信息。

## 十二、防内存溢出（OOM）设计

从代码、配置、监控、优化四个维度防止内存溢出，确保系统稳定运行。

### 1. 代码层优化

- 避免大对象创建：1. 富文本内容分段加载（而非一次性加载）；2. 分页查询（默认每页 20 条，限制最大页数）；3. 避免循环创建大量对象（使用对象池）；
- 关闭资源：1. IO 流、数据库连接、Redis 连接通过 try-with-resources 自动关闭；2. MyBatis 的 SqlSession、ResultSet 及时关闭；
- 避免内存泄漏：1. 静态集合（如 Map）及时清理过期数据；2. 线程池使用完后关闭（或设置核心线程数）；3. 避免匿名内部类持有外部类引用。

### 2. 配置层优化

- JVM 参数配置（生产环境）：

  ```plaintext
  -Xms4g -Xmx4g # 堆内存初始/最大值（根据服务器配置调整，如8核16G服务器设为8g）
  -XX:NewRatio=2 # 新生代:老年代=1:2
  -XX:SurvivorRatio=8 # Eden:Survivor=8:1
  -XX:+UseG1GC # 使用G1垃圾收集器（高效回收大对象）
  -XX:+HeapDumpOnOutOfMemoryError # OOM时生成堆转储文件
  -XX:HeapDumpPath=/var/log/csharp-tutorial/heapdump.hprof # 堆转储文件路径
  -XX:MaxDirectMemorySize=1g # 直接内存上限
  ```

  

- 缓存配置：1. Caffeine 缓存设置最大容量（如 10000 条）和过期时间（如 1 小时）；2. 避免缓存大对象（如教程富文本内容不缓存，仅缓存标题 / 简介）；

- 线程池配置：1. 核心线程数 = CPU 核心数 + 1，最大线程数 = 2*CPU 核心数；2. 设置队列容量（如 1000），拒绝策略为 CallerRunsPolicy（避免任务丢失）。

### 3. 监控层设计

- 集成 Spring Boot Actuator：暴露`/actuator/metrics/jvm.memory.used`（内存使用量）、`/actuator/metrics/jvm.threads.live`（活跃线程数）等端点；
- 自定义内存监控：`MemoryMonitorUtil`定时（每 5 分钟）监控堆内存使用率，超过 85% 时记录 WARN 日志，超过 95% 时触发告警；
- 堆转储分析：OOM 后通过 MAT（Memory Analyzer Tool）分析堆转储文件，定位内存泄漏点。

### 4. 业务层优化

- 异步处理：耗时操作（如日志记录、数据统计）通过`@Async`异步执行，避免阻塞主线程；
- 批量操作：数据库批量插入 / 更新（如批量导入教程），使用 MyBatis 的`foreach`批量处理，避免单条循环操作；
- 定时清理：通过`@Scheduled`定时清理临时文件、过期缓存、未审核的垃圾评论，释放内存。

## 十三、部署与运维补充

### 1. 环境隔离

- 开发环境（dev）：本地开发，连接测试数据库，日志级别 DEBUG；
- 测试环境（test）：服务器部署，连接测试数据库，日志级别 INFO，开启性能监控；
- 生产环境（prod）：服务器集群部署，连接生产数据库，日志级别 WARN/ERROR，开启 HTTPS、权限控制、日志归档。

### 2. 数据库运维

- 定时备份：每天凌晨备份 MySQL 数据库，保留 7 天备份文件；
- 慢查询监控：开启 MySQL 慢查询日志（阈值 1 秒），定期分析慢 SQL 并优化；
- 分表策略：教程内容表（tutorial_content）数据量超过 100 万条时，按年份分表（如 tutorial_content_2025）。

### 3. 系统扩展性

- 接口标准化：RESTful API 设计，便于后续前端分离改造（如替换 JSP 为 Vue/React）；
- 模块化设计：分类、章节、内容等功能模块化，便于后续新增功能（如教程点赞、收藏）；
- 多环境配置：使用 Spring Boot Profiles 实现配置隔离，避免修改代码切换环境。

## 十四、总结

本方案基于 MVC 架构，整合 JSP+SpringBoot+MySQL 技术栈，覆盖了实体设计、数据库设计、项目结构、版本控制、加密、工具类、页面、日志、防 OOM 等全维度要求，复刻微软 C# 教程页面的风格和交互逻辑。方案注重安全性（加密、权限控制）、稳定性（防 OOM、日志监控）、可维护性（模块化、版本控制），可直接作为项目开发的核心指导文档，后续可根据实际业务需求补充细节（如新增教程搜索、用户收藏等功能）。
