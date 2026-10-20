package com.example.teachingmanagement.dto;

import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class EvaluationDto {
    private Long teamId;
    private Long criterionId;
    private Long activityId;
    private Double singlePoints; // For PER_TEAM evaluations
    private String feedback;
    private Map<Long, Double> studentPoints = new HashMap<>(); // For PER_STUDENT evaluations

    public EvaluationDto() {
    }

    public EvaluationDto(Long teamId, Long criterionId, Long activityId, Double points, String feedback) {
        this.teamId = teamId;
        this.criterionId = criterionId;
        this.activityId = activityId;
        this.singlePoints = points;
        this.feedback = feedback;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(Long criterionId) {
        this.criterionId = criterionId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    // Handle the "points" field from JSON - could be a number or a map
    @JsonProperty("points")
    public void setPointsFromJson(Object value) {
        if (value instanceof Number) {
            this.singlePoints = ((Number) value).doubleValue();
            this.studentPoints.clear();
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> map = (Map<Object, Object>) value;
            this.studentPoints.clear();
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                Long studentId = Long.valueOf(entry.getKey().toString());
                Double points = Double.valueOf(entry.getValue().toString());
                this.studentPoints.put(studentId, points);
            }
            this.singlePoints = null;
        }
    }
    
    public Map<Long, Double> getStudentPoints() {
        return studentPoints;
    }
    
    public void setStudentPoints(Map<Long, Double> studentPoints) {
        this.studentPoints = studentPoints;
    }

    public Double getSinglePoints() {
        return singlePoints;
    }

    public void setSinglePoints(Double singlePoints) {
        this.singlePoints = singlePoints;
    }
    
    // Check if this is per-student evaluation
    public boolean isPerStudent() {
        return studentPoints != null && !studentPoints.isEmpty();
    }
}
