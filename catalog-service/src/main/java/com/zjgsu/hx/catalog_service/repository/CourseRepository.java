package com.zjgsu.hx.catalog_service.repository;

import com.zjgsu.hx.catalog_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    /**
     * 按课程代码查询（唯一）
     */
    Optional<Course> findByCourseId(String courseId);

    /**
     * 按课程标题关键字模糊查询（忽略大小写）
     */
    List<Course> findByTitleContainingIgnoreCase(String keyword);

    /**
     * 查询容量还有剩余的课程（capacity > enrolled）
     * 用 JPQL 自定义查询
     */
    @Query("SELECT c FROM Course c WHERE c.enrolled < c.capacity")
    List<Course> findCoursesWithAvailableSeats();

    /**
     * 按讲师编号查询课程
     */
    @Query("SELECT c FROM Course c WHERE c.instructor.instructorId = :instructorId")
    List<Course> findByInstructorId(String instructorId);

    /**
     * 判重：课程编号是否存在
     */
    boolean existsByCourseId(String courseId);
}
