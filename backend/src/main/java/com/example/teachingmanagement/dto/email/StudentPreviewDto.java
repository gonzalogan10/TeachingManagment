package com.example.teachingmanagement.dto.email;

import java.util.List;

public class StudentPreviewDto {

    private final String fullName;
    private final String email;
    private final List<CriterionResultDto> results;
    private final double total;

    public StudentPreviewDto(String fullName, String email, List<CriterionResultDto> results, double total) {
        this.fullName = fullName;
        this.email = email;
        this.results = results;
        this.total = total;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public List<CriterionResultDto> getResults() {
        return results;
    }

    public double getTotal() {
        return total;
    }
}