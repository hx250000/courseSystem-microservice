# 校园选课系统--微服务版（School Course Management System - Microservices Version）

# 一、项目简介
项目名称：校园选课系统--微服务版（School Course Management System - Microservices Version）
项目版本：1.2.0
基于版本：1.1.0（单体应用版）
微服务架构说明：本系统模拟了高校的**选课业务逻辑**，实现了学生管理、课程管理与选课管理等模块，其中，将课程管理单独拆分、学生管理和选课管理单独拆分，形成两个微服务。  
# 二、微服务架构图
客户端
  ↓
  ├─→ catalog-service (8081) → catalog_db (3307)
  │   └── 课程管理
  │
  └─→ enrollment-service (8082) → enrollment_db (3308)
      ├── 学生管理
      ├── 选课管理
      └── HTTP调用 → catalog-service（验证课程）
# 三、技术栈
 - Spring Boot 3.5.7
 - Java 25
 - MySQL 8.4
 - Docker & Docker Compose
 - RestTemplate（服务间通信）
# 四、环境要求
 - JDK 25+
 - Maven 3.8+
 - Docker 20.10+
 - Docker Compose 2.0+
# 五、项目结构
course-microservice
C:.
│  .dockerignore
│  .gitattributes
│  .gitignore
│  coursetest.openapi.json
│  docker-compose.yml
│  Dockerfile
│  HELP.md
│  readme.md
│  test-service.sh
│
├─catalog-service
│  │  .dockerignore
│  │  .gitignore
│  │  Dockerfile
│  │  mvnw
│  │  mvnw.cmd
│  │  pom.xml
│  │
│  ├─.idea
│  │      .gitignore
│  │      ApifoxUploaderProjectSetting.xml
│  │      compiler.xml
│  │      encodings.xml
│  │      jarRepositories.xml
│  │      misc.xml
│  │      uiDesigner.xml
│  │      workspace.xml
│  │
│  └─src
│    └─main
│      ├─java
│      │  └─com
│      │      └─zjgsu
│      │          └─hx
│      │              └─catalog_service
│      │                  │  catalog_serviceApplication.java
│      │                  │
│      │                  ├─common
│      │                  │      ApiResponse.java
│      │                  │
│      │                  ├─controller
│      │                  │      CourseController.java
│      │                  │
│      │                  ├─exception
│      │                  │      GlobalExceptionHandler.java
│      │                  │      ResourceConflictException.java
│      │                  │      ResourceNotFoundException.java
│      │                  │
│      │                  ├─model
│      │                  │      Course.java
│      │                  │      Instructor.java
│      │                  │      ScheduleSlot.java
│      │                  │
│      │                  ├─repository
│      │                  │      CourseRepository.java
│      │                  │
│      │                  └─service
│      │                          CourseService.java
│      │
│      └─resources
│          │  application-docker.yml
│          │  application.yml
│          │
│          ├─db
│          │      data.sql
│          │      schema.sql
│          │
│          ├─static
│          └─templates
│  
└─enrollment-service
    │  .dockerignore
    │  .gitignore
    │  Dockerfile
    │  mvnw
    │  mvnw.cmd
    │  pom.xml
    │
    └─src
      └─main
        ├─java
        │  └─com
        │      └─zjgsu
        │          └─hx
        │              └─enrollment_service
        │                  │  enrollment_serviceApplication.java
        │                  │
        │                  ├─common
        │                  │      ApiResponse.java
        │                  │
        │                  ├─controller
        │                  │      EnrollmentController.java
        │                  │      StudentController.java
        │                  │
        │                  ├─exception
        │                  │      GlobalExceptionHandler.java
        │                  │      ResourceConflictException.java
        │                  │      ResourceNotFoundException.java
        │                  │
        │                  ├─model
        │                  │      Enrollment.java
        │                  │      Status.java
        │                  │      Student.java
        │                  │
        │                  ├─repository
        │                  │      EnrollmentRepository.java
        │                  │      StudentRepository.java
        │                  │
        │                  └─service
        │                          EnrollmentService.java
        │                          StudentService.java
        │
        └─resources
            │  application-docker.yml
            │  application.yml
            │
            ├─db
            │      data.sql
            │      schema.sql
            │
            ├─static
            └─templates
      

# 六、构建和运行步骤
在项目根目录下，运行```docker compose build```构建应用
然后运行```docker compose up```运行
通过curl或者在浏览器里面输入http://hocalhost:8081/访问

# 五、接口说明（RESTful Api）
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

