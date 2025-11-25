# 校园选课系统（School Course Management System）

# 一、系统概述

本系统模拟了高校的**选课业务逻辑**，实现了学生管理、课程管理与选课管理三大模块。  
采用 Spring Boot 架构，以 **RESTful API** 的形式提供统一接口，支持标准 JSON 格式输入输出。  

系统数据通过MySQL数据库进行存储，实现数据持久化。

# 二、系统架构设计

java/com.zjgsu.hx.schoolcoursesimple
 ├── controller/         # 控制层：处理 HTTP 请求与响应
 │   ├── StudentController.java
 │   ├── CourseController.java
 │   ├── EnrollmentController.java
 |   └── HealthController.java
 ├── service/            # 业务逻辑层：实现核心功能与校验
 │   ├── StudentService.java
 │   ├── CourseService.java
 │   └── EnrollmentService.java
 ├── repository/         # 数据访问层：使用 Map 模拟存储
 │   ├── StudentRepository.java
 │   ├── CourseRepository.java
 │   └── EnrollmentRepository.java
 ├── model/              # 实体类：学生、课程、选课信息
 │   ├── Student.java
 │   ├── Course.java
 │   ├── Instructor.java
 │   ├── ScheduleSlot.java
 │   ├── Status.java
 │   └── Enrollment.java
 ├── exception/          # 异常类与全局异常处理
 │   ├── GlobalExceptionHandler.java
 │   ├── ResourceNotFoundException.java
 │   └── BusinessException.java
 ├── common/
 │   └── ApiResponse.java   # 统一响应体
 └── SchollcoursesimpleApplication.java
resources
 ├── db/         # 控制层：处理 HTTP 请求与响应
 │   ├── schema.sql      # 数据库建表语句
 |   └── data.sql        # 初始化数据
 └── application.yml 

# 三、功能设计

| 模块     | 功能说明                                                   |
| -------- | ---------------------------------------------------------- |
| 学生管理 | 新增、修改、删除、查询学生信息；删除前检查是否存在选课记录 |
| 课程管理 | 新增、修改、删除、查询课程信息；删除前检查是否存在选课记录 |
| 选课管理 | 学生选课、退课、查询选课记录；检查容量上限与重复选课       |
| 异常处理 | 统一异常返回机制；邮箱格式验证；容量、重复、缺失校验       |

# 四、数据库设计
表结构概览
- Student
| id | student_id | name | major | grade | email | created_at |
- Course
| id | course_id | title | capacity | enrolled | instructor_id | name | email | day_of_week | start_time | end_time | expected_attendance | created_at |
- Enrollment
| id | student_id | course_id | status | created_at |

# 五、接口说明（RESTful Api）
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

# 六、异常处理机制

系统通过 `GlobalExceptionHandler` 统一捕获异常并以 `ApiResponse` 格式返回。

| 异常类型                    | 返回码 | 示例说明                       |
| --------------------------- | ------ | ------------------------------ |
| `ResourceNotFoundException` | 404    | 学生或课程不存在               |
| `IllegalArgumentException`  | 400    | 参数错误，如邮箱格式           |
| `ResourceConflictException` | 409    | 业务冲突，如容量已满、重复选课 |
| 其他未捕获异常              | 500    | 系统内部错误                   |

返回示例：

```
{
  "code": 409,
  "message": "选课失败：课程容量已满",
  "data": null,
  "timestamp": "2025-10-25T22:50:23.241"
}
```

# 七、运行环境与配置

| 项目        | 版本             |
| ----------- | ---------------- |
| JDK         | 17+              |
| Spring Boot | 3.x              |
| MySQL      | 8.0              |
| 构建工具    | Maven            |
| 测试工具    | Postman / Apifox |
| 端口号      | 8080             |

## 启动方式
```bash
mvn spring-boot:run
```
启动后访问：

```bash
http://localhost:8080
```

或者直接在IntelliJ IDEA中启动项目

## 数据初始化
- 启动应用，自动生成数据库表结构，或运行src/main/resources/db/schema.sql生成表结构
- 运行src/main/resources/db/data.sql插入初始化数据



