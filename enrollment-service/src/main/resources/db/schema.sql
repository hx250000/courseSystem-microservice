USE course_db;

-- 删除旧表（防止重复执行报错）
DROP TABLE IF EXISTS enrollment;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS course;

-- 学生表
CREATE TABLE student (
    id VARCHAR(50) PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    major VARCHAR(100),
    grade INT,
    email VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 课程表
CREATE TABLE course (
    id VARCHAR(50) PRIMARY KEY,
    course_id VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    enrolled INT DEFAULT 0,
    email VARCHAR(100) NOT NULL,
    instructor_id VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    day_of_week VARCHAR(50) NOT NULL,
    start_time VARCHAR(50) NOT NULL,
    end_time VARCHAR(50) NOT NULL,
    expected_attendance INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 选课表
CREATE TABLE enrollment (
    id VARCHAR(50) PRIMARY KEY,
    studentid VARCHAR(50) NOT NULL,
    courseid VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student FOREIGN KEY (studentid) REFERENCES student(id) ON DELETE CASCADE,
    CONSTRAINT fk_course FOREIGN KEY (courseid) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT unique_student_course UNIQUE (studentid, courseid)
);
