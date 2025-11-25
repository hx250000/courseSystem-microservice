package com.zjgsu.hx.enrollment_service.repository;

import com.zjgsu.hx.enrollment_service.model.Enrollment;
import com.zjgsu.hx.enrollment_service.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    /**
     * 按学生查询选课记录
     */
    List<Enrollment> findByStudentId(String studentId);

    /**
     * 按课程查询选课记录
     */
    List<Enrollment> findByCourseId(String courseId);

    /**
     * 按学生和课程联合查询（判断是否重复选课）
     */
    Optional<Enrollment> findByStudentIdAndCourseId(String studentId, String courseId);

    /**
     * 判断是否存在该选课关系
     */
    boolean existsByStudentIdAndCourseId(String studentId, String courseId);

    /**
     * 删除某学生与课程的选课记录
     */
    void deleteByStudentIdAndCourseId(String studentId, String courseId);

    boolean existsByCourseId(String courseId);
}
