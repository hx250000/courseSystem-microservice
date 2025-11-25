USE course_db;

-- 删除旧表（防止重复执行报错）
DROP TABLE IF EXISTS enrollment;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS course;

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

