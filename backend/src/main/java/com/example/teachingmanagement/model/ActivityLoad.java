package com.example.teachingmanagement.model;

public class ActivityLoad {

    private Long activityId;
    private double totalHours;
    private int totalStudents;
    private double totalProportion;

    public ActivityLoad() {
    }

    public ActivityLoad(Long activityId, double totalHours, int totalStudents, double totalProportion) {
        this.activityId = activityId;
        this.totalHours = totalHours;
        this.totalStudents = totalStudents;
        this.totalProportion = totalProportion;
    }

    // Getters and Setters
    public Long getActivityId() {
        return activityId;
    }   

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public double getTotalProportion() {
        return totalProportion;
    }

    public void setTotalProportion(double totalProportion) {
        this.totalProportion = totalProportion;
    }    
}
