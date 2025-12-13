package com.zjgsu.hx.enrollment_service.dto;

import lombok.Data;

@Data
public class TeacherDto {
    private String id;          // 继承自 Users
    private String username;    // 继承自 Users
    private String email;       // 继承自 Users

    private String teacherId;
    private String name;
    private String department;

    //private UserType userType = UserType.TEACHER;
}
