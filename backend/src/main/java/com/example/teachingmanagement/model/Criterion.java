package com.example.teachingmanagement.model;

import jakarta.persistence.*;

@Entity
public class Criterion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String checkingProcedure;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationMethod evaluationMethod;

    @Column(nullable = false)
    private double maxPoints;

    @Column(nullable = false)
    private Boolean pointsRedistribution = false;

    @Column(length = 2000)
    private String defaultFeedback;

    // Constructors
    public Criterion() {
    }

    public Criterion(String category, String name, String checkingProcedure, 
                     EvaluationMethod evaluationMethod, double maxPoints, 
                     Boolean pointsRedistribution, String defaultFeedback) {
        this.category = category;
        this.name = name;
        this.checkingProcedure = checkingProcedure;
        this.evaluationMethod = evaluationMethod;
        this.maxPoints = maxPoints;
        this.pointsRedistribution = pointsRedistribution != null ? pointsRedistribution : false;
        this.defaultFeedback = defaultFeedback;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckingProcedure() {
        return checkingProcedure;
    }

    public void setCheckingProcedure(String checkingProcedure) {
        this.checkingProcedure = checkingProcedure;
    }

    public EvaluationMethod getEvaluationMethod() {
        return evaluationMethod;
    }

    public void setEvaluationMethod(EvaluationMethod evaluationMethod) {
        this.evaluationMethod = evaluationMethod;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }

    public boolean isPointsRedistribution() {
        return pointsRedistribution != null ? pointsRedistribution : false;
    }

    public void setPointsRedistribution(Boolean pointsRedistribution) {
        this.pointsRedistribution = pointsRedistribution != null ? pointsRedistribution : false;
    }

    public String getDefaultFeedback() {
        return defaultFeedback;
    }

    public void setDefaultFeedback(String defaultFeedback) {
        this.defaultFeedback = defaultFeedback;
    }
}
