package com.zjgsu.hx.enrollment_service.client;

import com.zjgsu.hx.enrollment_service.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(
        name = "catalog-service",
        contextId = "catalogClient"
)
public interface CatalogClient {
    @GetMapping("/api/courses/{courseId}")
    CourseDto getCourse(String courseId);

    @PutMapping("/api/courses/{courseId}/increment")
    CourseDto increaseEnrolledCount(String courseId);

    @PutMapping("/api/courses/{courseId}/decrement")
    CourseDto decreaseEnrolledCount(String courseId);
}
