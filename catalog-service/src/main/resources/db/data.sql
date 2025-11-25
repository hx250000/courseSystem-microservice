USE catalog_db;
-- 初始化学生表

-- 初始化课程表
INSERT INTO course (id, course_id, title, capacity, enrolled, email, instructor_id, name, day_of_week, start_time, end_time, expected_attendance, created_at) VALUES
(UUID(), 'CS101', 'Java 程序设计', 60, 0, 'zhanglaoshi@zjgsu.edu.cn', 'T001', '张老师', '周一', '08:00', '09:40', 0, NOW()),
(UUID(), 'CS102', '数据库原理', 50, 0, 'lilaoshi@zjgsu.edu.cn', 'T002', '李老师', '周三', '10:00', '11:40', 0, NOW()),
(UUID(), 'CS103', 'Web 应用开发', 45, 0, 'wulaoshi@zjgsu.edu.cn', 'T003', '吴老师', '周五', '14:00', '15:40', 0, NOW());


