package com.example.teachingmanagement.model;

import jakarta.persistence.*;

@Entity
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Criterion criterion;

    @ManyToOne(optional = false)
    private TeamMembership membership;

    private Long activityId; // To filter evaluations by activity

    private Double points;

    @Column(length = 2000)
    private String feedback;

    public Assessment() {
    }

    public Assessment(Criterion criterion, Double points, String feedback) {
        this.criterion = criterion;
        this.points = points;
        this.feedback = feedback;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Criterion getCriterion() {
        return criterion;
    }

    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }

    public TeamMembership getTeamMembership() {
        return membership;
    }

    public void setTeamMembership(TeamMembership membership) {
        this.membership = membership;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}