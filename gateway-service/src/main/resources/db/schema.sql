-- 使用数据库（根据环境调整）
USE course_db;

-- 删除旧表（防止重复执行报错）
DROP TABLE IF EXISTS `enrollment`;
DROP TABLE IF EXISTS `course`;
DROP TABLE IF EXISTS `user`;

-- 单表继承：用户表（包含学生与教师）
CREATE TABLE `user` (
    id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100),
    email VARCHAR(100) NOT NULL,
    user_type VARCHAR(50) NOT NULL, -- 对应 JPA 中的 @DiscriminatorColumn

    -- 学生/教师共用字段（可为空）
    student_id VARCHAR(50) UNIQUE,
    teacher_id VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    major VARCHAR(100),
    grade INT,
    department VARCHAR(100),

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_email UNIQUE (email)
);


