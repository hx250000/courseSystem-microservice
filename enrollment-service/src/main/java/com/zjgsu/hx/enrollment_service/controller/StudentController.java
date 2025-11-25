package com.zjgsu.hx.enrollment_service.controller;

import com.zjgsu.hx.enrollment_service.model.Student;
import com.zjgsu.hx.enrollment_service.service.StudentService;
import com.zjgsu.hx.enrollment_service.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ApiResponse<List<Student>> getAllStudents() {
        return ApiResponse.success(studentService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Student> getStudentById(@PathVariable String id) {
        return ApiResponse.success(studentService.findById(id));
    }

    @PostMapping
    public ApiResponse<Student> createStudent(@RequestBody Student student) {
        return ApiResponse.success(studentService.createStudent(student));
    }

    @PutMapping("/{id}")
    public ApiResponse<Student> updateStudent(@PathVariable String id, @RequestBody Student student) {
        return ApiResponse.success(studentService.updateStudent(id,student));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Student> deleteStudent(@PathVariable String id) {
        return ApiResponse.success(studentService.deleteById(id));
    }
}
