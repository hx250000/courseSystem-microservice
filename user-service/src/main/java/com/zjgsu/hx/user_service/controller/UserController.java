package com.zjgsu.hx.user_service.controller;

import com.zjgsu.hx.user_service.model.Student;
import com.zjgsu.hx.user_service.model.Teacher;
import com.zjgsu.hx.user_service.service.UserService;
import com.zjgsu.hx.user_service.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/students")
    public ApiResponse<List<Student>> getAllStudents() {
        return ApiResponse.success(userService.findAllStudents());
    }

    @GetMapping("/teachers")
    public ApiResponse<List<Teacher>> getAllTeachers() {return ApiResponse.success(userService.findAllTeachers());}

    @GetMapping("/students/{id}")
    public ApiResponse<Student> getStudentByStudentId(@PathVariable String id) {
        return ApiResponse.success(userService.findStudentByStudentId(id));
    }

    @GetMapping("/teachers/{id}")
    public ApiResponse<Teacher> getTeacherByTeacherId(@PathVariable String id) {
        return ApiResponse.success(userService.findTeacherByTeacherId(id));
    }

    @PostMapping("/students")
    public ApiResponse<Student> createStudent(@RequestBody Student student) {
        return ApiResponse.success(userService.createStudent(student));
    }

    @PostMapping("/teachers")
    public ApiResponse<Teacher> createTeacher(@RequestBody Teacher teacher) {
        return ApiResponse.success(userService.createTeacher(teacher));
    }

    @PutMapping("/students/{id}")
    public ApiResponse<Student> updateStudent(@PathVariable String id, @RequestBody Student student) {
        return ApiResponse.success(userService.updateStudent(id,student));
    }

    @PutMapping("/teachers/{id}")
    public ApiResponse<Teacher> updateTeacher(@PathVariable String id, @RequestBody Teacher teacher) {
        return ApiResponse.success(userService.updateTeacher(id,teacher));
    }

    @DeleteMapping("/students/{id}")
    public ApiResponse<Student> deleteStudent(@PathVariable String id) {
        return ApiResponse.success(userService.deleteStudentById(id));
    }

    @DeleteMapping("/teachers/{id}")
    public ApiResponse<Teacher> deleteTeacher(@PathVariable String id) {
        return ApiResponse.success(userService.deleteTeacherById(id));
    }


}
