package com.zjgsu.hx.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("STUDENT")
@Data
public class Student extends Users{
    @Column(name = "student_id")
    private String studentId;
    private String name;
    private String major;
    private Integer grade;
}
