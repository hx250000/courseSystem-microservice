package com.zjgsu.hx.catalog_service.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class ScheduleSlot {
    //private String id;
    private String dayOfWeek;       // MONDAY, TUESDAY 等
    private String startTime;       // "08:00"
    private String endTime;         // "10:00"
    private int expectedAttendance;
    //private LocalDateTime createAt;

    public ScheduleSlot() {
        //this.setId();
        //this.setCreateAt();
    }

    public ScheduleSlot(String dayOfWeek, String startTime, String endTime, int expectedAttendance) {
        //this.setId();
        this.setDayOfWeek(dayOfWeek);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setExpectedAttendance(expectedAttendance);
        //this.setCreateAt();
    }

    /*public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }*/

    /*public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt() {
        this.createAt = LocalDateTime.now();
    }*/

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getExpectedAttendance() {
        return expectedAttendance;
    }

    public void setExpectedAttendance(int expectedAttendance) {
        this.expectedAttendance = expectedAttendance;
    }
}
