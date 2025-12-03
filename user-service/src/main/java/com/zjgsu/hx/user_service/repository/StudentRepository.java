package com.zjgsu.hx.user_service.repository;

import com.zjgsu.hx.user_service.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    /* 按学号查询学生（用于登录或查找） */
    Optional<Student> findByStudentId(String studentId);

    /* 按邮箱查询学生（用于判重或找回账号） */
    Optional<Student> findByEmail(String email);

    /* 检查学号是否存在（用于创建时判重） */
    boolean existsByStudentId(String studentId);

    /* 检查邮箱是否存在（用于创建时判重） */
    boolean existsByEmail(String email);

    /* 按专业查询学生（用于筛选） */
    List<Student> findByMajor(String major);

    /* 按年级查询学生（用于筛选） */
    List<Student> findByGrade(int grade);

    /* 根据学号删除学生 */
    void deleteByStudentId(String studentId);
}
