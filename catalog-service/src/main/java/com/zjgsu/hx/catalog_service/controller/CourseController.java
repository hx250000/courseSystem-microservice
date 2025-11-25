package com.zjgsu.hx.catalog_service.controller;

import com.zjgsu.hx.catalog_service.model.Course;
import com.zjgsu.hx.catalog_service.service.CourseService;
import com.zjgsu.hx.catalog_service.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ApiResponse<List<Course>> getAllCourses() {
        return ApiResponse.success(courseService.findAll());
    }

    @GetMapping("/{courseId}")
    public ApiResponse<Course> getCourseByCourseId(@PathVariable String courseId) {
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
}
