package com.zjgsu.hx.catalog_service.controller;

import com.zjgsu.hx.catalog_service.model.Course;
import com.zjgsu.hx.catalog_service.service.CourseService;
import com.zjgsu.hx.catalog_service.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@Slf4j
public class CourseController {
    private final CourseService courseService;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    public String test() {
        return "[User Service Instance: " +
                serviceName +
                ", Port: " +
                port + "]";
    }

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ApiResponse<List<Course>> getAllCourses() {
        return ApiResponse.success(courseService.findAll());
    }

    @GetMapping("/{courseId}")
    public ApiResponse<Course> getCourseByCourseId(@PathVariable String courseId) {
        log.info("[{}:{}] handling getCourse {}", serviceName, port, courseId);
        return ApiResponse.success(courseService.findByCourseId(courseId));
    }

    @PostMapping
    public ApiResponse<Course> createCourse(@RequestBody Course course) {
        return ApiResponse.success(courseService.createCourse(course));
    }

    @PutMapping("/{courseId}")
    public ApiResponse<Course> updateCourse(@PathVariable String courseId, @RequestBody Course course) {
        return ApiResponse.success(courseService.updateCourse(courseId,course));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Course> deleteCourse(@PathVariable String id) {
        return ApiResponse.success(courseService.deleteById(id));
    }

    @PutMapping("/{courseId}/increment")
    public ApiResponse<Course> increamentCourse(@PathVariable String courseId) {
        return ApiResponse.success(courseService.increaseEnrolledCount(courseId));
    }

    @PutMapping("/{courseId}/decrement")
    public ApiResponse<Course> decreaseCourse(@PathVariable String courseId) {
        return ApiResponse.success(courseService.decreaseEnrolledCount(courseId));
    }
}
