USE enrollment_db;
-- 初始化学生表
INSERT INTO student (id, student_id, name, major, grade, email, created_at) VALUES
(UUID(), '20250001', '张三', '计算机科学与技术', 3, 'zhangsan@zjgsu.edu.cn', NOW()),
(UUID(), '20250002', '李四', '软件工程', 2, 'lisi@zjgsu.edu.cn', NOW()),
(UUID(), '20250003', '王五', '信息管理', 1, 'wangwu@zjgsu.edu.cn', NOW());


-- 初始化选课表（学生选课程）
-- 使用子查询确保外键一致
INSERT INTO enrollment (id, student_id, course_id, status, created_at)
VALUES
(UUID(),
 (SELECT id FROM student WHERE student_id='20250001'),
 (SELECT id FROM course WHERE course_id='CS101'),
 'ACTIVE', NOW()),
(UUID(),
 (SELECT id FROM student WHERE student_id='20250002'),
 (SELECT id FROM course WHERE course_id='CS102'),
 'ACTIVE', NOW()),
(UUID(),
 (SELECT id FROM student WHERE student_id='20250001'),
 (SELECT id FROM course WHERE course_id='CS103'),
 'ACTIVE', NOW());
