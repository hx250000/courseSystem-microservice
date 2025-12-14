package com.zjgsu.hx.catalog_service.client;

import com.zjgsu.hx.catalog_service.common.ApiResponse;
import com.zjgsu.hx.catalog_service.dto.EnrollmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "enrollment-service",
        contextId = "enrollmentClient"
)
public interface EnrollmentClient {
    // 1️⃣ 远程调用：根据学生 ID 获取选课记录列表
    @GetMapping("/api/enrollments/student/{studentId}")
    ApiResponse<List<EnrollmentDto>> getEnrollmentsByStudentId(
            @PathVariable("studentId") String studentId
    );

    // 2️⃣ 远程调用：根据课程 ID 获取选课记录列表
    @GetMapping("/api/enrollments/course/{courseId}")
    ApiResponse<List<EnrollmentDto>> getEnrollmentsByCourseId(
            @PathVariable("courseId") String courseId
    );
}
