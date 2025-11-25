package com.zjgsu.hx.enrollment_service.controller;

import com.zjgsu.hx.enrollment_service.model.Enrollment;
import com.zjgsu.hx.enrollment_service.service.EnrollmentService;
import com.zjgsu.hx.enrollment_service.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ApiResponse<List<Enrollment>> getAllEnrollments() {
        return ApiResponse.success(enrollmentService.findAll());
    }

    @GetMapping("/student/{studentId}")
    public ApiResponse<List<Enrollment>> getEnrollmentsByStudentId(@PathVariable String studentId) {
        return ApiResponse.success(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ApiResponse<List<Enrollment>> getEnrollmentsByCourseId(@PathVariable String courseId) {
        return ApiResponse.success(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    @PostMapping
    public ApiResponse<Enrollment> createEnrollment(@RequestParam String studentId,
                                                    @RequestParam String courseId) {
        return ApiResponse.success(enrollmentService.createEnrollment(studentId, courseId));
    }

    @DeleteMapping
    public ApiResponse<Enrollment> deleteEnrollment(@RequestParam String studentId,
                                                    @RequestParam String courseId) {
        return ApiResponse.success(enrollmentService.deleteEnrollment(studentId, courseId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Enrollment> deleteEnrollmentById(@PathVariable String id) {
        return ApiResponse.success(enrollmentService.deleteById(id));
    }
}
