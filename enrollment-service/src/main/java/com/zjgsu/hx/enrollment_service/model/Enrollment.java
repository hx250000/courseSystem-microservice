package com.zjgsu.hx.enrollment_service.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="enrollment",uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id", "student_id"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /*private String studentId;
    private String courseId;*/

    //@ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "student_id", nullable = false)
    private String studentId;//关联学生



    @Column(name = "course_id", nullable = false)
    private String courseId;

    @Enumerated(EnumType.STRING)
    private Status status;   // 选课状态（ACTIVE, DROPPED, COMPLETED）

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }

    public Enrollment() {

    }

    public Enrollment(String studentId, String courseId) {
        this.courseId=courseId;
        this.studentId=studentId;

        /*this.studentId = studentId;
        this.courseId = courseId;*/
    }

    public String getId() { return id; }
    public void setId() { this.id = UUID.randomUUID().toString(); }
    /*public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }*/
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt() { this.createdAt = LocalDateTime.now(); }
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}


}
