package com.example.teachingmanagement.dto.email;

import java.util.List;

public class TeamEmailPreviewsDto {

    private final String course;
    private final String activity;
    private final String team;
    private final List<EmailPreviewDto> emails;

    public TeamEmailPreviewsDto(
            String course,
            String activity,
            String team,
            List<EmailPreviewDto> emails
    ) {
        this.course = course;
        this.activity = activity;
        this.team = team;
        this.emails = emails;
    }

    public String getCourse() { return course; }
    public String getActivity() { return activity; }
    public String getTeam() { return team; }
    public List<EmailPreviewDto> getEmails() { return emails; }
}