package com.zjgsu.hx.enrollment_service.dto;

import lombok.Data;

@Data
public class CourseDto {
    //参考Course实体类
    private String id;
    private String courseId;
    private String title;
    private String instructorId;
    private String name;
    private String email;
    private String startTime;
    private String endTime;
    private String daysOfWeek;
    private Integer capacity;
    private Integer enrolled;
    private Integer expectedAttendance;
}
