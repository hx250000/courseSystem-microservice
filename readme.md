# 校园选课系统--微服务版（School Course Management System - Microservices Version）

# 一、项目简介
项目名称：校园选课系统--微服务版（School Course Management System - Microservices Version）
项目版本：1.2.0
基于版本：1.1.0（单体应用版）
微服务架构说明：本系统模拟了高校的**选课业务逻辑**，实现了学生管理、课程管理与选课管理等模块，其中，将课程管理单独拆分、学生管理和选课管理单独拆分，形成两个微服务。  
# 二、微服务架构图
```
                    ┌─────────────┐
                    │   客户端     │
                    └──────┬───────┘
                           │
                           │ HTTP
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│ catalog-service│  │ user-service  │  │enrollment-service│
│   (8081)       │  │   (8082)      │  │   (8083)         │
│                │  │               │  │                  │
│ 课程管理       │  │ 用户管理       │  │ 选课管理         │
│                │  │ 学生管理       │  │                  │
└───────┬───────┘  └───────┬───────┘  └───────┬──────────┘
        │                  │                  │
        │                  │                  │
        ▼                  ▼                  ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│  catalog_db   │  │   user_db     │  │ enrollment_db │
│   (3307)      │  │   (3309)      │  │   (3308)      │
└───────────────┘  └───────────────┘  └───────────────┘
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
                           ▼
                    ┌───────────────┐
                    │     Nacos     │
                    │   (8848)      │
                    │ 服务注册与发现 │
                    └───────────────┘
```
# 三、技术栈
 - Spring Boot 3.2.7
 - Spring Cloud 2023.0.3
 - Spring Cloud Alibaba 2023.0.1.0
 - Nacos 3.1.0（服务注册与发现）
 - Java 17
 - MySQL 8
 - Docker & Docker Compose
 - RestTemplate（服务间通信）
 - Spring Data JPA（数据持久化）
# 四、环境要求
 - JDK 17+
 - Maven 3.8+
 - Docker 20.10+
 - Docker Compose 2.0+
# 五、项目结构
```
course-microservice/
│
├── catalog-service/              # 课程目录服务
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/zjgsu/hx/catalog_service/
│   │       │       ├── catalog_serviceApplication.java
│   │       │       ├── controller/        # 控制器层
│   │       │       ├── service/           # 业务逻辑层
│   │       │       ├── repository/        # 数据访问层
│   │       │       ├── model/             # 数据模型
│   │       │       ├── exception/         # 异常处理
│   │       │       └── common/            # 公共类
│   │       └── resources/
│   │           ├── application.yml        # 配置文件
│   │           └── db/                    # 数据库脚本
│   ├── Dockerfile
│   └── pom.xml
│
├── enrollment-service/            # 选课服务
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/zjgsu/hx/enrollment_service/
│   │       │       ├── enrollment_serviceApplication.java
│   │       │       ├── controller/
│   │       │       ├── service/
│   │       │       ├── repository/
│   │       │       ├── model/
│   │       │       ├── exception/
│   │       │       └── common/
│   │       └── resources/
│   │           ├── application.yml
│   │           └── db/
│   ├── Dockerfile
│   └── pom.xml
│
├── user-service/                  # 用户服务
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/zjgsu/hx/user_service/
│   │       │       ├── user_serviceApplication.java
│   │       │       ├── controller/
│   │       │       ├── service/
│   │       │       ├── repository/
│   │       │       ├── model/
│   │       │       ├── exception/
│   │       │       └── common/
│   │       └── resources/
│   │           ├── application.yml
│   │           └── db/
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml             # Docker Compose 配置
├── API接口文档.md                  # API 接口文档
└── readme.md                      # 项目说明文档
```

# 六、构建和运行步骤

## 6.1 使用 Docker Compose 一键部署

在项目根目录下，执行以下命令：

```bash
# 构建所有服务镜像
docker compose build

# 启动所有服务（包括 Nacos、数据库和微服务）
docker compose up -d

# 查看服务运行状态
docker compose ps

# 查看服务日志
docker compose logs -f [service-name]
```

启动成功后，可通过以下地址访问：
- **Nacos 控制台**：http://localhost:8848/nacos（默认用户名/密码：nacos/nacos）
- **课程服务**：http://localhost:8081
- **用户服务**：http://localhost:8082
- **选课服务**：http://localhost:8083

## 6.2 Nacos 部署说明

本项目使用 Nacos 作为服务注册与发现中心，Nacos 已集成在 `docker-compose.yml` 中，配置如下：

- **镜像版本**：nacos/nacos-server:v3.1.0
- **运行模式**：standalone（单机模式）
- **端口映射**：
  - 8848：Nacos 主端口（服务注册与发现）
  - 9848：Nacos gRPC 端口
  - 8080：Nacos 控制台端口（已映射到 8848）
- **命名空间**：dev
- **服务组**：COURSEHUB_GROUP

### 服务注册配置

各微服务通过 Spring Cloud Alibaba Nacos Discovery 自动注册到 Nacos，配置示例：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: nacos:8848
        namespace: dev
        group: COURSEHUB_GROUP
        ephemeral: true
```

启动服务后，可在 Nacos 控制台的"服务管理 -> 服务列表"中查看已注册的服务实例。

### 停止服务

```bash
# 停止所有服务
docker compose down

# 停止并删除数据卷（谨慎使用）
docker compose down -v
```

# 七、接口说明（RESTful Api）
详细接口信息见[API接口文档.md]
## 系统结构
### Controller层
接收 HTTP 请求、调用 Service 层、返回统一响应
### Service层
实现业务逻辑、数据验证、规则控制
### Repository层
连接数据库，负责数据的存储与读取
### Model层
系统所需的数据结构模型
## 1. 学生模块 `/students`

| 操作         | 方法   | URL              | 说明                     |
| ------------ | ------ | ---------------- | ------------------------ |
| 查询所有学生 | GET    | `/students`      | 获取全部学生信息         |
| 查询单个学生 | GET    | `/students/{id}` | 通过 id 查询             |
| 新增学生     | POST   | `/students`      | 创建新学生               |
| 更新学生     | PUT    | `/students/{id}` | 修改学生信息             |
| 删除学生     | DELETE | `/students/{id}` | 若存在选课记录则禁止删除 |

示例请求（POST）：

```
{
  "studentId": "2023001",
  "name": "张三",
  "email": "zs@xx.com",
  "grade": 2023
  "major": "计算机科学"
}
```

## 2. 课程模块 `/courses`

| 操作         | 方法   | URL             | 说明                         |
| ------------ | ------ | --------------- | ---------------------------- |
| 查询课程     | GET    | `/courses`      | 获取所有课程                 |
| 查询单个课程 | GET    | `/courses/{id}` | 按 ID 获取                   |
| 新增课程     | POST   | `/courses`      | 创建课程（需课程代码与名称） |
| 更新课程     | PUT    | `/courses/{id}` | 修改课程信息                 |
| 删除课程     | DELETE | `/courses/{id}` | 若存在选课记录则禁止删除     |

示例请求（POST）：

```
{
  "courseId": "C001",
  "title": "操作系统",
  "capacity": 60,
  "instructor": {},
  "scheduleslot": {}
}
```

## 3. 选课模块 `/enrollments`

| 操作         | 方法   | URL                                            | 说明             |
| ------------ | ------ | ---------------------------------------------- | ---------------- |
| 查询所有选课 | GET    | `/enrollments`                                 | 获取所有选课记录 |
| 按学生查询   | GET    | `/enrollments/student/{studentId}`             | 某学生的选课     |
| 按课程查询   | GET    | `/enrollments/course/{courseId}`               | 某课程的学生列表 |
| 学生选课     | POST   | `/enrollments?studentId=2023001&courseId=C001` | 学生选课         |
| 学生退课     | DELETE | `/enrollments` | 学生退课         |
| 删除选课记录 | DELETE | `/enrollments/{id}`                            | 管理员删除       |

### 4. 健康检查 `/health`
| 操作         | 方法   | URL             | 说明                         |
| ------------ | ------ | --------------- | ---------------------------- |
| 查询数据库连接状态     | GET    | `/db`      | 获取数据库当前连接状态                 |

返回示例：

```
{
  "code": 200,
  "message": "✅ 数据库连接正常",
  "data": null,
  "timestamp": "2025-11-02T22:35:08.365"
}
```

# 八、注意事项

1. **数据库初始化**：首次启动时，数据库表结构会自动创建（通过 JPA 的 `ddl-auto: update`）
2. **服务启动顺序**：Nacos 会先启动，各微服务会等待数据库健康检查通过后再启动
3. **服务间通信**：微服务之间通过服务名（如 `catalog-service`）进行通信，无需硬编码 IP 地址
4. **数据持久化**：数据库数据存储在 Docker 数据卷中，删除容器不会丢失数据（除非使用 `docker compose down -v`）
5. **端口占用**：确保本地端口 8081、8082、8083、8848、3307、3308、3309 未被占用

