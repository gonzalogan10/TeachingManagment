package com.example.teachingmanagement.controller;

import com.example.teachingmanagement.dto.email.TeamEmailPreviewsDto;
import com.example.teachingmanagement.service.email.EmailPreviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email-previews")
public class EmailPreviewController {

    private final EmailPreviewService service;

    public EmailPreviewController(EmailPreviewService service) {
        this.service = service;
    }

    /*** Returns the email preview for an assessment for a team in particular
     * @param teamId ID of the team
     * @return the team
     */
    @GetMapping("/team/{teamId}")
    public TeamEmailPreviewsDto previewEmails(@PathVariable Long teamId) {
        return service.buildTeamEmailPreviews(teamId);
    }
}