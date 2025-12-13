package com.zjgsu.hx.enrollment_service.client;

import com.zjgsu.hx.enrollment_service.dto.StudentDto;
import com.zjgsu.hx.enrollment_service.dto.TeacherDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        contextId = "userClient",
        fallback = UserClientFallback.class
)
public interface UserClient {

    @GetMapping("/api/users/students/{id}")
    StudentDto getStudent(@PathVariable("id") String id);

    @GetMapping("/api/users/teachers/{id}")
    TeacherDto getTeacher(@PathVariable("id") String id);

}
