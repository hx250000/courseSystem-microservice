# 校园选课系统 API 接口文档

## 项目概述

本项目是一个基于微服务架构的校园选课系统，包含三个核心服务：
- **catalog-service**：课程目录服务，负责课程信息的管理
- **enrollment-service**：选课服务，负责选课记录的管理
- **user-service**：用户服务，负责学生信息的管理

## 服务信息

| 服务名称 | 端口 | 基础路径 | 数据库 |
|---------|------|---------|--------|
| catalog-service | 8081 | /api/courses | catalog_db |
| user-service | 8082 | /api/users/students | user_db |
| enrollment-service | 8083 | /api/enrollments | enrollment_db |

## 统一响应格式

所有接口均返回统一的 `ApiResponse<T>` 格式：

```json
{
  "code": 200,
  "message": "Success",
  "data": {},
  "timestamp": "2024-01-01T12:00:00"
}
```

### 响应字段说明

| 字段 | 类型 | 说明 |
|-----|------|------|
| code | Integer | HTTP状态码（200-成功，400-客户端错误，404-资源未找到，409-资源冲突，500-服务器错误） |
| message | String | 响应消息 |
| data | Object | 响应数据（泛型，根据接口返回不同类型） |
| timestamp | LocalDateTime | 响应时间戳 |

### 状态码说明

| 状态码 | 说明 |
|-------|------|
| 200 | 请求成功 |
| 201 | 资源创建成功 |
| 400 | 客户端请求错误（参数错误等） |
| 404 | 资源未找到 |
| 409 | 资源冲突（如重复创建） |
| 500 | 服务器内部错误 |

---

## 一、Catalog Service (课程目录服务)

### 基础路径
```
http://localhost:8081/api/courses
```

---

### 1.1 获取所有课程

**接口描述**：获取系统中的所有课程列表

**请求方式**：`GET`

**请求路径**：`/api/courses`

**请求参数**：无

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": "uuid",
      "courseId": "CS101",
      "title": "数据结构",
      "instructor": {
        "instructorId": "T001",
        "name": "张老师",
        "email": "zhang@example.com"
      },
      "scheduleSlot": {
        "dayOfWeek": "MONDAY",
        "startTime": "08:00",
        "endTime": "10:00",
        "expectedAttendance": 50
      },
      "capacity": 50,
      "enrolled": 30,
      "createdAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T12:00:00"
}
```

---

### 1.2 根据课程ID获取课程

**接口描述**：根据课程ID获取指定课程的详细信息

**请求方式**：`GET`

**请求路径**：`/api/courses/{courseId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| courseId | String | 是 | 课程ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "courseId": "CS101",
    "title": "数据结构",
    "instructor": {
      "instructorId": "T001",
      "name": "张老师",
      "email": "zhang@example.com"
    },
    "scheduleSlot": {
      "dayOfWeek": "MONDAY",
      "startTime": "08:00",
      "endTime": "10:00",
      "expectedAttendance": 50
    },
    "capacity": 50,
    "enrolled": 30,
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程不存在）：
```json
{
  "code": 404,
  "message": "课程不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

### 1.3 创建课程

**接口描述**：创建新的课程

**请求方式**：`POST`

**请求路径**：`/api/courses`

**请求头**：
```
Content-Type: application/json
```

**请求体**：
```json
{
  "courseId": "CS101",
  "title": "数据结构",
  "instructor": {
    "instructorId": "T001",
    "name": "张老师",
    "email": "zhang@example.com"
  },
  "scheduleSlot": {
    "dayOfWeek": "MONDAY",
    "startTime": "08:00",
    "endTime": "10:00",
    "expectedAttendance": 50
  },
  "capacity": 50
}
```

**请求体字段说明**：

| 字段 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| courseId | String | 是 | 课程ID（唯一） |
| title | String | 是 | 课程名称 |
| instructor | Object | 是 | 授课教师信息 |
| instructor.instructorId | String | 是 | 教师ID |
| instructor.name | String | 是 | 教师姓名 |
| instructor.email | String | 是 | 教师邮箱 |
| scheduleSlot | Object | 是 | 课程时间安排 |
| scheduleSlot.dayOfWeek | String | 是 | 星期几（如：MONDAY, TUESDAY等） |
| scheduleSlot.startTime | String | 是 | 开始时间（格式：HH:mm） |
| scheduleSlot.endTime | String | 是 | 结束时间（格式：HH:mm） |
| scheduleSlot.expectedAttendance | Integer | 是 | 预期出勤人数 |
| capacity | Integer | 是 | 课程容量 |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "generated-uuid",
    "courseId": "CS101",
    "title": "数据结构",
    "instructor": {
      "instructorId": "T001",
      "name": "张老师",
      "email": "zhang@example.com"
    },
    "scheduleSlot": {
      "dayOfWeek": "MONDAY",
      "startTime": "08:00",
      "endTime": "10:00",
      "expectedAttendance": 50
    },
    "capacity": 50,
    "enrolled": 0,
    "createdAt": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程代码已存在）：
```json
{
  "code": 400,
  "message": "课程代码已存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（参数验证失败）：
```json
{
  "code": 400,
  "message": "课程代码和名称不能为空！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**其他可能的错误消息**：
- "课程信息不能为空！"
- "课程代码不能为空！"
- "课程名称不能为空！"
- "授课教师不能为空！"
- "课程时间安排不能为空！"
- "课程容量必须大于 0！"

---

### 1.4 更新课程

**接口描述**：更新指定课程的信息

**请求方式**：`PUT`

**请求路径**：`/api/courses/{courseId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| courseId | String | 是 | 课程ID |

**请求头**：
```
Content-Type: application/json
```

**请求体**：
```json
{
  "courseId": "CS101",
  "title": "高级数据结构",
  "instructor": {
    "instructorId": "T001",
    "name": "张老师",
    "email": "zhang@example.com"
  },
  "scheduleSlot": {
    "dayOfWeek": "TUESDAY",
    "startTime": "09:00",
    "endTime": "11:00",
    "expectedAttendance": 50
  },
  "capacity": 60
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "courseId": "CS101",
    "title": "高级数据结构",
    "instructor": {
      "instructorId": "T001",
      "name": "张老师",
      "email": "zhang@example.com"
    },
    "scheduleSlot": {
      "dayOfWeek": "TUESDAY",
      "startTime": "09:00",
      "endTime": "11:00",
      "expectedAttendance": 50
    },
    "capacity": 60,
    "enrolled": 30,
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程不存在）：
```json
{
  "code": 404,
  "message": "课程不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（参数验证失败）：
```json
{
  "code": 400,
  "message": "课程代码和名称不能为空！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

### 1.5 删除课程

**接口描述**：根据课程ID删除指定课程

**请求方式**：`DELETE`

**请求路径**：`/api/courses/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| id | String | 是 | 课程的UUID（不是courseId） |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "courseId": "CS101",
    "title": "数据结构",
    "instructor": {
      "instructorId": "T001",
      "name": "张老师",
      "email": "zhang@example.com"
    },
    "scheduleSlot": {
      "dayOfWeek": "MONDAY",
      "startTime": "08:00",
      "endTime": "10:00",
      "expectedAttendance": 50
    },
    "capacity": 50,
    "enrolled": 30,
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程不存在）：
```json
{
  "code": 404,
  "message": "课程不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（存在选课记录，无法删除）：
```json
{
  "code": 409,
  "message": "无法删除：该课程存在选课记录！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

### 1.6 增加课程已选人数

**接口描述**：增加指定课程的已选人数（由 enrollment-service 调用）

**请求方式**：`PUT`

**请求路径**：`/api/courses/{courseId}/increment`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| courseId | String | 是 | 课程ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "courseId": "CS101",
    "title": "数据结构",
    "instructor": {
      "instructorId": "T001",
      "name": "张老师",
      "email": "zhang@example.com"
    },
    "scheduleSlot": {
      "dayOfWeek": "MONDAY",
      "startTime": "08:00",
      "endTime": "10:00",
      "expectedAttendance": 50
    },
    "capacity": 50,
    "enrolled": 31,
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程不存在）：
```json
{
  "code": 404,
  "message": "课程不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程人数已满）：
```json
{
  "code": 409,
  "message": "课程人数已满！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

### 1.7 减少课程已选人数

**接口描述**：减少指定课程的已选人数（由 enrollment-service 调用）

**请求方式**：`PUT`

**请求路径**：`/api/courses/{courseId}/decrement`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| courseId | String | 是 | 课程ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "courseId": "CS101",
    "title": "数据结构",
    "instructor": {
      "instructorId": "T001",
      "name": "张老师",
      "email": "zhang@example.com"
    },
    "scheduleSlot": {
      "dayOfWeek": "MONDAY",
      "startTime": "08:00",
      "endTime": "10:00",
      "expectedAttendance": 50
    },
    "capacity": 50,
    "enrolled": 29,
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程不存在）：
```json
{
  "code": 404,
  "message": "课程不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程人数已空）：
```json
{
  "code": 409,
  "message": "课程人数已空！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

## 二、Enrollment Service (选课服务)

### 2.1 学生管理接口

#### 说明
学生信息存储在 user-service 中。enrollment-service **不直接提供**学生管理接口，而是通过远程调用 user-service 验证学生是否存在。以下列出的学生管理接口是 **user-service 提供的 API**，enrollment-service 在创建/查询选课记录时会调用这些接口进行学生验证。

#### 基础路径
```
http://localhost:8082/api/users/students
```

---

#### 2.1.1 获取所有学生

**接口描述**：获取系统中的所有学生列表（实际由 user-service 提供）

**请求方式**：`GET`

**请求路径**：`/api/users/students`

**请求参数**：无

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": "uuid",
      "studentId": "S001",
      "name": "张三",
      "major": "计算机科学",
      "grade": 2023,
      "email": "zhangsan@example.com",
      "createAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T12:00:00"
}
```

---

#### 2.1.2 根据ID获取学生

**接口描述**：根据学生ID获取指定学生的详细信息（实际由 user-service 提供）

**请求方式**：`GET`

**请求路径**：`/api/users/students/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| id | String | 是 | 学生的UUID |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "studentId": "S001",
    "name": "张三",
    "major": "计算机科学",
    "grade": 2023,
    "email": "zhangsan@example.com",
    "createAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（学生不存在）：
```json
{
  "code": 404,
  "message": "学生不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

#### 2.1.3 创建学生

**接口描述**：创建新的学生信息（实际由 user-service 提供）

**请求方式**：`POST`

**请求路径**：`/api/users/students`

**请求头**：
```
Content-Type: application/json
```

**请求体**：
```json
{
  "studentId": "S001",
  "name": "张三",
  "major": "计算机科学",
  "grade": 2023,
  "email": "zhangsan@example.com"
}
```

**请求体字段说明**：

| 字段 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| studentId | String | 是 | 学生ID（唯一） |
| name | String | 是 | 学生姓名 |
| major | String | 否 | 专业 |
| grade | Integer | 否 | 年级 |
| email | String | 是 | 邮箱 |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "generated-uuid",
    "studentId": "S001",
    "name": "张三",
    "major": "计算机科学",
    "grade": 2023,
    "email": "zhangsan@example.com",
    "createAt": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（学号已存在）：
```json
{
  "code": 409,
  "message": "该学号已存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（邮箱已被注册）：
```json
{
  "code": 409,
  "message": "该邮箱已被注册！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（邮箱格式错误）：
```json
{
  "code": 400,
  "message": "邮箱格式不正确！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（邮箱为空）：
```json
{
  "code": 400,
  "message": "邮箱不能为空！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

#### 2.1.4 更新学生

**接口描述**：更新指定学生的信息（实际由 user-service 提供）

**请求方式**：`PUT`

**请求路径**：`/api/users/students/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| id | String | 是 | 学生的UUID |

**请求头**：
```
Content-Type: application/json
```

**请求体**：
```json
{
  "studentId": "S001",
  "name": "张三",
  "major": "软件工程",
  "grade": 2023,
  "email": "zhangsan_new@example.com"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "studentId": "S001",
    "name": "张三",
    "major": "软件工程",
    "grade": 2023,
    "email": "zhangsan_new@example.com",
    "createAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（学生不存在）：
```json
{
  "code": 404,
  "message": "学生不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

#### 2.1.5 删除学生

**接口描述**：根据ID删除指定学生（实际由 user-service 提供）

**请求方式**：`DELETE`

**请求路径**：`/api/users/students/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| id | String | 是 | 学生的UUID |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "studentId": "S001",
    "name": "张三",
    "major": "计算机科学",
    "grade": 2023,
    "email": "zhangsan@example.com",
    "createAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（学生不存在）：
```json
{
  "code": 404,
  "message": "学生不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

### 2.2 选课管理接口

#### 基础路径
```
http://localhost:8083/api/enrollments
```

---

#### 2.2.1 获取所有选课记录

**接口描述**：获取系统中的所有选课记录

**请求方式**：`GET`

**请求路径**：`/api/enrollments`

**请求参数**：无

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": "uuid",
      "studentId": "S001",
      "courseId": "CS101",
      "status": "ACTIVE",
      "createdAt": "2024-01-01T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T12:00:00"
}
```

---

#### 2.2.2 根据学生ID获取选课记录

**接口描述**：获取指定学生的所有选课记录

**请求方式**：`GET`

**请求路径**：`/api/enrollments/student/{studentId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| studentId | String | 是 | 学生ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": "uuid",
      "studentId": "S001",
      "courseId": "CS101",
      "status": "ACTIVE",
      "createdAt": "2024-01-01T10:00:00"
    },
    {
      "id": "uuid2",
      "studentId": "S001",
      "courseId": "CS102",
      "status": "ACTIVE",
      "createdAt": "2024-01-02T10:00:00"
    }
  ],
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（学生不存在）：
```json
{
  "code": 404,
  "message": "学生不存在：S001",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

#### 2.2.3 根据课程ID获取选课记录

**接口描述**：获取选择指定课程的所有学生的选课记录

**请求方式**：`GET`

**请求路径**：`/api/enrollments/course/{courseId}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| courseId | String | 是 | 课程ID |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": [
    {
      "id": "uuid",
      "studentId": "S001",
      "courseId": "CS101",
      "status": "ACTIVE",
      "createdAt": "2024-01-01T10:00:00"
    },
    {
      "id": "uuid2",
      "studentId": "S002",
      "courseId": "CS101",
      "status": "ACTIVE",
      "createdAt": "2024-01-01T11:00:00"
    }
  ],
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程不存在）：
```json
{
  "code": 404,
  "message": "课程不存在：CS101",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

#### 2.2.4 创建选课记录

**接口描述**：学生选择课程，创建选课记录

**请求方式**：`POST`

**请求路径**：`/api/enrollments`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| studentId | String | 是 | 学生ID |
| courseId | String | 是 | 课程ID |

**请求示例**：
```
POST /api/enrollments?studentId=S001&courseId=CS101
```

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "generated-uuid",
    "studentId": "S001",
    "courseId": "CS101",
    "status": "ACTIVE",
    "createdAt": "2024-01-01T12:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（学生不存在）：
```json
{
  "code": 404,
  "message": "学生不存在：S001",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（课程不存在）：
```json
{
  "code": 404,
  "message": "课程不存在：CS101",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（已选该课程且状态为 ACTIVE）：
```json
{
  "code": 409,
  "message": "该学生已选择该课程！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**注意**：如果学生曾选过该课程但已退课（status=DROPPED），系统会将其状态恢复为 ACTIVE，而不是抛出错误。

**业务说明**：
- 系统会验证学生是否存在（通过调用 user-service 的 `/api/users/students/{studentId}` 接口）
- 系统会验证课程是否存在（通过调用 catalog-service 的 `/api/courses/{courseId}` 接口）
- 如果学生已选该课程且状态为 ACTIVE，返回 409 冲突错误
- 如果学生已选该课程但状态为 DROPPED，系统会恢复选课记录为 ACTIVE 状态
- 选课成功后，系统会调用 catalog-service 的增量接口（`/api/courses/{courseId}/increment`）来更新课程已选人数
- increment 接口会检查课程容量，如果已选人数达到容量上限，会抛出"课程人数已满！"错误

---

#### 2.2.5 丢弃选课记录（通过学生ID和课程ID）

**接口描述**：学生退课，将指定学生和课程的选课记录标记为 DROPPED 状态（不删除记录），并自动更新课程已选人数

**业务说明**：
- 系统会验证学生是否存在（通过调用 user-service）
- 系统会查找对应的选课记录
- 如果选课记录不存在，返回 404 错误
- 将选课记录的 status 设置为 DROPPED（不会物理删除）
- 调用 catalog-service 的 decrement 接口（`/api/courses/{courseId}/decrement`）来减少课程已选人数
- decrement 接口会检查已选人数，如果已选人数为 0，会抛出"课程人数已空！"错误

**请求方式**：`DELETE`

**请求路径**：`/api/enrollments`

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| studentId | String | 是 | 学生ID |
| courseId | String | 是 | 课程ID |

**请求示例**：
```
DELETE /api/enrollments?studentId=S001&courseId=CS101
```

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "studentId": "S001",
    "courseId": "CS101",
    "status": "DROPPED",
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（学生不存在）：
```json
{
  "code": 404,
  "message": "学生不存在：S001",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（选课记录不存在）：
```json
{
  "code": 404,
  "message": "选课记录不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

#### 2.2.6 丢弃选课记录（通过ID）

**接口描述**：根据选课记录ID物理删除选课记录，并自动更新课程已选人数

**业务说明**：
- 系统会根据ID查找选课记录
- 如果选课记录不存在，返回 404 错误
- 获取选课记录对应的课程ID
- 调用 catalog-service 的 decrement 接口（`/api/courses/{courseId}/decrement`）来减少课程已选人数
- 物理删除选课记录（与 deleteEnrollment 方法不同，此方法会真正删除记录）

**请求方式**：`DELETE`

**请求路径**：`/api/enrollments/{id}`

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| id | String | 是 | 选课记录的UUID |

**响应示例**：
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "id": "uuid",
    "studentId": "S001",
    "courseId": "CS101",
    "status": "DROPPED",
    "createdAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

**错误响应**（选课记录不存在）：
```json
{
  "code": 404,
  "message": "选课记录不存在!",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

## 三、数据模型

### 3.1 Course（课程）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | String (UUID) | 主键，自动生成 |
| courseId | String | 课程ID（唯一标识） |
| title | String | 课程名称 |
| instructor | Instructor | 授课教师信息（嵌入对象） |
| scheduleSlot | ScheduleSlot | 课程时间安排（嵌入对象） |
| capacity | Integer | 课程容量 |
| enrolled | Integer | 已选人数 |
| createdAt | LocalDateTime | 创建时间 |

### 3.2 Instructor（教师）

| 字段 | 类型 | 说明 |
|-----|------|------|
| instructorId | String | 教师ID |
| name | String | 教师姓名 |
| email | String | 教师邮箱 |

### 3.3 ScheduleSlot（课程时间）

| 字段 | 类型 | 说明 |
|-----|------|------|
| dayOfWeek | String | 星期几（如：MONDAY, TUESDAY等） |
| startTime | String | 开始时间（格式：HH:mm） |
| endTime | String | 结束时间（格式：HH:mm） |
| expectedAttendance | Integer | 预期出勤人数 |

### 3.4 Student（学生）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | String (UUID) | 主键，自动生成 |
| studentId | String | 学生ID（唯一标识） |
| name | String | 学生姓名 |
| major | String | 专业 |
| grade | Integer | 年级 |
| email | String | 邮箱 |
| createAt | LocalDateTime | 创建时间 |

### 3.5 Enrollment（选课记录）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | String (UUID) | 主键，自动生成 |
| studentId | String | 学生ID |
| courseId | String | 课程ID |
| status | Status (枚举) | 选课状态（ACTIVE-正在选，DROPPED-已退课，COMPLETED-已修完） |
| createdAt | LocalDateTime | 创建时间 |

### 3.6 Status（选课状态枚举）

| 值 | 说明 |
|----|------|
| ACTIVE | 正在选课 |
| DROPPED | 已退课 |
| COMPLETED | 已修完 |

---

## 四、错误处理

### 4.1 常见错误码

| 状态码 | 说明 | 示例消息 |
|-------|------|---------|
| 400 | 客户端请求错误 | "课程代码和名称不能为空！"、"邮箱格式不正确！" |
| 404 | 资源未找到 | "课程不存在！"、"学生不存在！"、"选课记录不存在！" |
| 409 | 资源冲突 | "课程代码已存在！"、"该学号已存在！"、"课程人数已满！"、"无法删除：该课程存在选课记录！" |
| 500 | 服务器内部错误 | "Internal server error: ..." |

### 4.2 错误响应格式

```json
{
  "code": 404,
  "message": "课程不存在！",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

### 4.3 常见错误消息汇总

#### Catalog Service 错误消息

| 错误类型 | 状态码 | 错误消息 |
|---------|-------|---------|
| 课程不存在 | 404 | "课程不存在！" |
| 课程代码已存在 | 400 | "课程代码已存在！" |
| 课程信息为空 | 400 | "课程信息不能为空！" |
| 课程代码为空 | 400 | "课程代码不能为空！" |
| 课程名称为空 | 400 | "课程名称不能为空！" |
| 授课教师为空 | 400 | "授课教师不能为空！" |
| 时间安排为空 | 400 | "课程时间安排不能为空！" |
| 容量无效 | 400 | "课程容量必须大于 0！" |
| 存在选课记录 | 409 | "无法删除：该课程存在选课记录！" |
| 课程人数已满 | 409 | "课程人数已满！" |
| 课程人数已空 | 409 | "课程人数已空！" |

#### Enrollment Service 错误消息

**学生管理相关：**

| 错误类型 | 状态码 | 错误消息 |
|---------|-------|---------|
| 学生不存在 | 404 | "学生不存在！"（user-service 返回） |
| 学号已存在 | 409 | "该学号已存在！"、"学号已存在！" |
| 邮箱已被注册 | 409 | "该邮箱已被注册！" |
| 邮箱为空 | 400 | "邮箱不能为空！" |
| 邮箱格式错误 | 400 | "邮箱格式不正确！" |
| 存在选课记录 | 409 | "无法删除：该学生存在选课记录！" |

**选课管理相关：**

| 错误类型 | 状态码 | 错误消息 |
|---------|-------|---------|
| 学生不存在 | 404 | "学生不存在：{studentId}" |
| 课程不存在 | 404 | "课程不存在：{courseId}" |
| 选课记录不存在 | 404 | "选课记录不存在！"、"选课记录不存在!" |
| 已选该课程（ACTIVE） | 409 | "该学生已选择该课程！" |

---

## 五、接口测试示例

### 5.1 使用 cURL 测试

#### 获取所有课程
```bash
curl -X GET http://localhost:8081/api/courses
```

#### 创建课程
```bash
curl -X POST http://localhost:8081/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "CS101",
    "title": "数据结构",
    "instructor": {
      "instructorId": "T001",
      "name": "张老师",
      "email": "zhang@example.com"
    },
    "scheduleSlot": {
      "dayOfWeek": "MONDAY",
      "startTime": "08:00",
      "endTime": "10:00",
      "expectedAttendance": 50
    },
    "capacity": 50
  }'
```

#### 创建学生
```bash
curl -X POST http://localhost:8082/api/users/students \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "S001",
    "name": "张三",
    "major": "计算机科学",
    "grade": 2023,
    "email": "zhangsan@example.com"
  }'
```

#### 选课
```bash
curl -X POST "http://localhost:8083/api/enrollments?studentId=S001&courseId=CS101"
```

#### 退课
```bash
curl -X DELETE "http://localhost:8083/api/enrollments?studentId=S001&courseId=CS101"
```

---

## 六、注意事项

1. **服务端口**：
   - catalog-service 运行在 8081 端口
   - enrollment-service 运行在 8083 端口
   - user-service 运行在 8082 端口

2. **数据一致性**：
   - 选课操作会检查课程是否存在（通过调用 catalog-service）
   - 选课操作会检查学生是否存在（通过调用 user-service）
- 选课操作会自动更新课程已选人数（通过调用 catalog-service 的 increment 接口）
- 退课操作会自动更新课程已选人数（通过调用 catalog-service 的 decrement 接口）
- increment/decrement 接口会检查课程容量和已选人数，超出容量或已选人数为负时会抛出错误

3. **唯一性约束**：
   - courseId 必须唯一
   - studentId 必须唯一
   - 同一学生和课程的选课记录只能存在一条（studentId + courseId 唯一）

4. **自动字段**：
   - id（UUID）：自动生成
   - createdAt/createAt：自动设置为当前时间
   - enrolled：选课成功后调用 increment 接口递增，退课后调用 decrement 接口递减
   - status：创建选课记录时自动设置为 ACTIVE，退课時修改为 DROPPED

5. **删除操作**：
   - 删除课程使用 UUID（id），不是 courseId
   - 删除学生使用 UUID（id），不是 studentId
   - 删除选课记录有两种方式：
     - `DELETE /api/enrollments?studentId=xxx&courseId=xxx`：将选课记录状态改为 DROPPED（逻辑删除）
     - `DELETE /api/enrollments/{id}`：物理删除选课记录

6. **课程人数管理**：
   - increment 接口：增加课程已选人数，会检查是否超过容量
   - decrement 接口：减少课程已选人数，会检查是否为负数
   - 这两个接口由 enrollment-service 内部调用，不建议外部直接调用

7. **服务间调用**：
   - enrollment-service 通过 RestTemplate 调用 catalog-service 和 user-service
   - catalog-service 通过 RestTemplate 调用 enrollment-service（用于删除课程时检查是否有选课记录）

---





