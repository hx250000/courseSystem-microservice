# 校园选课系统 API 接口文档

## 项目概述

本项目是一个基于微服务架构的校园选课系统，包含两个核心服务：
- **catalog-service**：课程目录服务，负责课程信息的管理
- **enrollment-service**：选课服务，负责学生信息和选课记录的管理

## 服务信息

| 服务名称 | 端口 | 基础路径 | 数据库 |
|---------|------|---------|--------|
| catalog-service | 8081 | /api/courses | catalog_db |
| enrollment-service | 8082 | /api/enrollments, /api/students | enrollment_db |

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

## 二、Enrollment Service (选课服务)

### 2.1 学生管理接口

#### 基础路径
```
http://localhost:8082/api/students
```

---

#### 2.1.1 获取所有学生

**接口描述**：获取系统中的所有学生列表

**请求方式**：`GET`

**请求路径**：`/api/students`

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

**接口描述**：根据学生ID获取指定学生的详细信息

**请求方式**：`GET`

**请求路径**：`/api/students/{id}`

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

**接口描述**：创建新的学生信息

**请求方式**：`POST`

**请求路径**：`/api/students`

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

**接口描述**：更新指定学生的信息

**请求方式**：`PUT`

**请求路径**：`/api/students/{id}`

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

**接口描述**：根据ID删除指定学生

**请求方式**：`DELETE`

**请求路径**：`/api/students/{id}`

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
http://localhost:8082/api/enrollments
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
  "message": "学生不存在！",
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

**错误响应**（重复选课）：
```json
{
  "code": 409,
  "message": "Already enrolled in this course",
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

#### 2.2.5 删除选课记录（通过学生ID和课程ID）

**接口描述**：学生退课，删除指定学生和课程的选课记录

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
    "status": "ACTIVE",
    "createdAt": "2024-01-01T10:00:00"
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

**错误响应**（选课记录不存在）：
```json
{
  "code": 404,
  "message": "选课记录不存在！",
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

#### 2.2.6 删除选课记录（通过ID）

**接口描述**：根据选课记录ID删除选课记录

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
    "status": "ACTIVE",
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

**错误响应**（课程不存在）：
```json
{
  "code": 404,
  "message": "课程不存在：CS101",
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

#### Enrollment Service 错误消息

**学生管理相关：**

| 错误类型 | 状态码 | 错误消息 |
|---------|-------|---------|
| 学生不存在 | 404 | "学生不存在！" |
| 学号已存在 | 409 | "该学号已存在！"、"学号已存在！" |
| 邮箱已被注册 | 409 | "该邮箱已被注册！" |
| 邮箱为空 | 400 | "邮箱不能为空！" |
| 邮箱格式错误 | 400 | "邮箱格式不正确！" |
| 存在选课记录 | 409 | "无法删除：该学生存在选课记录！" |

**选课管理相关：**

| 错误类型 | 状态码 | 错误消息 |
|---------|-------|---------|
| 学生不存在 | 404 | "学生不存在！"、"学生不存在" |
| 课程不存在 | 404 | "课程不存在：{courseId}" |
| 选课记录不存在 | 404 | "选课记录不存在！"、"选课记录不存在!" |
| 课程人数已满 | 409 | "课程人数已满！" |
| 重复选课 | 409 | "Already enrolled in this course" |
| 课程人数已空 | 409 | "课程人数已空！" |

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
curl -X POST http://localhost:8082/api/students \
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
curl -X POST "http://localhost:8082/api/enrollments?studentId=S001&courseId=CS101"
```

#### 退课
```bash
curl -X DELETE "http://localhost:8082/api/enrollments?studentId=S001&courseId=CS101"
```

---

## 六、注意事项

1. **服务端口**：
   - catalog-service 运行在 8081 端口
   - enrollment-service 运行在 8082 端口

2. **数据一致性**：
   - 选课操作会检查课程是否存在（通过调用 catalog-service）
   - 选课操作会检查学生是否存在
   - 选课操作会检查课程是否已满

3. **唯一性约束**：
   - courseId 必须唯一
   - studentId 必须唯一
   - 同一学生和课程的选课记录只能存在一条（studentId + courseId 唯一）

4. **自动字段**：
   - id（UUID）：自动生成
   - createdAt/createAt：自动设置为当前时间
   - enrolled：选课时自动递增，退课时自动递减
   - status：创建选课记录时自动设置为 ACTIVE

5. **删除操作**：
   - 删除课程使用 UUID（id），不是 courseId
   - 删除学生使用 UUID（id），不是 studentId
   - 删除选课记录可以使用 UUID（id）或学生ID+课程ID

---





