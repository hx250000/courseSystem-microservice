package com.zjgsu.hx.enrollment_service.dto;

import lombok.Data;

@Data
public class StudentDto {
    private String id;          // 继承自 Users
    private String username;    // 继承自 Users
    private String email;       // 继承自 Users

    private String studentId;
    private String name;
    private String major;
    private Integer grade;

    //private UserType userType = UserType.STUDENT;
}
