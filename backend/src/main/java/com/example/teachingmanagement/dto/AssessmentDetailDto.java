package com.example.teachingmanagement.dto;

public class AssessmentDetailDto {
    private Long id;
    private Long criterionId;
    private String criterionName;
    private Long studentId;
    private String studentName;
    private Double points;
    private String feedback;
    private Integer teamSize;

    public AssessmentDetailDto() {
    }

    public AssessmentDetailDto(Long id, Long criterionId, String criterionName, Long studentId, String studentName, Double points, String feedback, Integer teamSize) {
        this.id = id;
        this.criterionId = criterionId;
        this.criterionName = criterionName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.points = points;
        this.feedback = feedback;
        this.teamSize = teamSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(Long criterionId) {
        this.criterionId = criterionId;
    }

    public String getCriterionName() {
        return criterionName;
    }

    public void setCriterionName(String criterionName) {
        this.criterionName = criterionName;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public Integer getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }
}
