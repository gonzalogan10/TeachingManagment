package com.example.teachingmanagement.dto.email;

import java.util.List;

public class TeamEmailPreviewDto {

    private final String course;
    private final String activity;
    private final String team;

    private final List<CriterionResultDto> teamResults;
    private final List<StudentPreviewDto> studentResults;

    private final double teamTotal;

    public TeamEmailPreviewDto(String course, String activity, String team, List<CriterionResultDto> teamResults,
                               List<StudentPreviewDto> studentResults, double teamTotal) {
        this.course = course;
        this.activity = activity;
        this.team = team;
        this.teamResults = teamResults;
        this.studentResults = studentResults;
        this.teamTotal = teamTotal;
    }

    public String getCourse() {
        return course;
    }

    public String getActivity() {
        return activity;
    }

    public String getTeam() {
        return team;
    }

    public List<CriterionResultDto> getTeamResults() {
        return teamResults;
    }

    public List<StudentPreviewDto> getStudentResults() {
        return studentResults;
    }

    public double getTeamTotal() {
        return teamTotal;
    }
}