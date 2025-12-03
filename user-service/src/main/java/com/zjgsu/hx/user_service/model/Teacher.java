package com.zjgsu.hx.user_service.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("TEACHER")
@Data
public class Teacher extends Users {
    @Column(name = "teacher_id")
    private String teacherId;
    private String name;
    private String department;
}
