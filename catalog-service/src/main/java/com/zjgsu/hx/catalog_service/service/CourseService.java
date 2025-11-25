package com.zjgsu.hx.catalog_service.service;

import com.zjgsu.hx.catalog_service.common.ApiResponse;
import com.zjgsu.hx.catalog_service.exception.ResourceConflictException;
import com.zjgsu.hx.catalog_service.exception.ResourceNotFoundException;
import com.zjgsu.hx.catalog_service.model.Course;
import com.zjgsu.hx.catalog_service.model.ScheduleSlot;
import com.zjgsu.hx.catalog_service.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class CourseService {
    private final CourseRepository courseRepository;
    @Value("${enrollment-service.url}")
    private String enrollmentServiceUrl;

    private final RestTemplate restTemplate;

    public CourseService(CourseRepository courseRepository, RestTemplate restTemplate) {
        this.courseRepository = courseRepository;
        this.restTemplate = restTemplate;
    }
    public List<Course> findAll() {
        return courseRepository.findAll();
    }
    public Course findById(String id) {
        return courseRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
    }
    public Course findByCourseId(String courseId) {
        return courseRepository.findByCourseId(courseId).
                orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
    }
    @Transactional
    public Course createCourse(Course course) {
        validateCourse(course, true);
        if (course.getCourseId() == null || course.getTitle() == null) {
            throw new IllegalArgumentException("课程代码和名称不能为空！");
        }
        boolean exists=courseRepository.existsByCourseId(course.getCourseId());
        if (exists) {
            throw new IllegalArgumentException("课程代码已存在！");
        }
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(String courseId,Course course) {

        Course existing = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("课程不存在！"));
        validateCourse(course, false);
        if (course.getCourseId() == null || course.getTitle() == null) {
            throw new IllegalArgumentException("课程代码和名称不能为空！");
        }

        ScheduleSlot slot=existing.getScheduleSlot();

        existing.setCourseId(course.getCourseId());
        existing.setTitle(course.getTitle());
        existing.setInstructor(course.getInstructor());
        existing.setCapacity(course.getCapacity());

        //existing.setScheduleSlot(course.getScheduleSlot());

        slot.setDayOfWeek(course.getScheduleSlot().getDayOfWeek());
        slot.setEndTime(course.getScheduleSlot().getEndTime());
        slot.setStartTime(course.getScheduleSlot().getStartTime());
        slot.setExpectedAttendance(course.getScheduleSlot().getExpectedAttendance());
        existing.setScheduleSlot(slot);

        return courseRepository.save(existing);
    }

    @Transactional
    public Course deleteById(String id) {
        Course course = courseRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("课程不存在！"));

        // 1. 调用 enrollment-service，获取该课程的所有选课记录
        String url = enrollmentServiceUrl + "/api/enrollments/course/" + id;

        ApiResponse<List<Map<String, Object>>> response =
                restTemplate.getForObject(url, ApiResponse.class);

        // enrollment-service 返回的数据在 response.getData()
        List<?> enrollments = response.getData();
        boolean hasEnrollments = enrollments != null && !enrollments.isEmpty();

//        boolean hasEnrollments = !enrollmentRepository
//                .findByCourse(course)
//                .isEmpty();
        if (hasEnrollments) {
            throw new ResourceConflictException("无法删除：该课程存在选课记录！");
        }
        courseRepository.deleteById(id);
        return course;
    }

//    @Transactional
//    public int increaseEnrollmentCount(String courseId) {
//        Course course = courseRepository.findByCourseId(courseId).
//                orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
//        if (course.getEnrolled()>=course.getCapacity()){
//            throw new ResourceConflictException("选课人数已满！");
//        }
//        course.addEnrolled();
//        courseRepository.save(course);
//        return course.getEnrolled();
//    }
//
//    @Transactional
//    public int decreaseEnrollmentCount(String courseId) {
//        Course course = courseRepository.findByCourseId(courseId).
//                orElseThrow(()->new ResourceNotFoundException("课程不存在！"));
//        if (course.getEnrolled()<=0){
//            throw new ResourceConflictException("选课人数已空！");
//        }
//        course.deleteEnrolled();
//        courseRepository.save(course);
//        return course.getEnrolled();
//    }

    private void validateCourse(Course course, boolean isCreating) {
        if (course == null) {
            throw new IllegalArgumentException("课程信息不能为空！");
        }
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty()) {
            throw new IllegalArgumentException("课程代码不能为空！");
        }
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("课程名称不能为空！");
        }
        if (course.getInstructor() == null) {
            throw new IllegalArgumentException("授课教师不能为空！");
        }
        if (course.getScheduleSlot() == null) {
            throw new IllegalArgumentException("课程时间安排不能为空！");
        }
        if (course.getCapacity() <= 0) {
            throw new IllegalArgumentException("课程容量必须大于 0！");
        }
    }
}
