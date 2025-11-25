package com.zjgsu.hx.catalog_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //自动生成UUID主键
    private String id; //uuid

    @Column(name = "course_id",unique = true, nullable = false)
    private String courseId; //课程id

    @Column(nullable = false)
    private String title; //课程名

    @Embedded
    private Instructor instructor;

    @Embedded
    private ScheduleSlot scheduleSlot;

    private int capacity;
    private int enrolled;

    @Column(name="createdAt",updatable=false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }

    public Course() {

    }

    public Course(String courseId, String title, Instructor instructor, ScheduleSlot scheduleSlot, int capacity) {

        this.courseId = courseId;
        this.title = title;
        this.instructor = instructor;
        this.scheduleSlot = scheduleSlot;
        this.capacity = capacity;
        this.enrolled = 0;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public ScheduleSlot getScheduleSlot() {
        return scheduleSlot;
    }

    public void setScheduleSlot(ScheduleSlot scheduleSlot) {
        this.scheduleSlot = scheduleSlot;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public void addEnrolled(){
        this.enrolled=this.enrolled+1;
    }

    public void deleteEnrolled(){
        this.enrolled=this.enrolled-1;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
