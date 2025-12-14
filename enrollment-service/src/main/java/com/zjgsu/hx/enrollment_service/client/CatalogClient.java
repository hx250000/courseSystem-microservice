package com.zjgsu.hx.enrollment_service.client;

import com.zjgsu.hx.enrollment_service.common.ApiResponse;
import com.zjgsu.hx.enrollment_service.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(
        name = "catalog-service",
        contextId = "catalogClient"
)
public interface CatalogClient {
    @GetMapping("/api/courses/{courseId}")
    ApiResponse<CourseDto> getCourse(@PathVariable("courseId") String courseId);

    @PutMapping("/api/courses/{courseId}/increment")
    ApiResponse<CourseDto> increaseEnrolledCount(@PathVariable("courseId") String courseId);

    @PutMapping("/api/courses/{courseId}/decrement")
    ApiResponse<CourseDto> decreaseEnrolledCount(@PathVariable("courseId") String courseId);
}
