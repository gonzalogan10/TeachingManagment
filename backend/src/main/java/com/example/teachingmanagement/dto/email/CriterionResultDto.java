package com.example.teachingmanagement.dto.email;

public class CriterionResultDto {

    private final String category;
    private final String name;
    private final double points;
    private final double maxPoints;
    private final String feedback;

    public CriterionResultDto(String category, String name, double points, double maxPoints, String feedback) {
        this.category = category;
        this.name = name;
        this.points = points;
        this.maxPoints = maxPoints;
        this.feedback = feedback;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public double getPoints() {
        return points;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public String getFeedback() {
        return feedback;
    }
}