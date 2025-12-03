package com.zjgsu.hx.user_service.repository;

import com.zjgsu.hx.user_service.model.Student;
import com.zjgsu.hx.user_service.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {

    /* 按学号查询学生（用于登录或查找） */
    Optional<Teacher> findByTeacherId(String studentId);

    /* 按邮箱查询学生（用于判重或找回账号） */
    Optional<Teacher> findByEmail(String email);

    /* 检查学号是否存在（用于创建时判重） */
    boolean existsByTeacherId(String teacherId);

    /* 检查邮箱是否存在（用于创建时判重） */
    boolean existsByEmail(String email);

    /* 根据学号删除学生 */
    void deleteByTeacherId(String teacherId);

    /* 根据部门查找 */
    Optional<Teacher> findByDepartment(String department);
}
