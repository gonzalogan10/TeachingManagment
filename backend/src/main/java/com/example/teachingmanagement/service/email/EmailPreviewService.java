package com.example.teachingmanagement.service.email;

import com.example.teachingmanagement.dto.email.CriterionResultDto;
import com.example.teachingmanagement.dto.email.StudentPreviewDto;
import com.example.teachingmanagement.dto.email.TeamEmailPreviewDto;
import com.example.teachingmanagement.dto.email.EmailPreviewDto;
import com.example.teachingmanagement.dto.email.TeamEmailPreviewsDto;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.teachingmanagement.model.*;
import com.example.teachingmanagement.repository.*;

import java.util.*;

@Service
public class EmailPreviewService {

    private final TeamRepository teamRepository;
    private final TeamMembershipRepository membershipRepository;
    private final AssessmentRepository assessmentRepository;

    public EmailPreviewService(TeamRepository teamRepository, TeamMembershipRepository membershipRepository,
                               AssessmentRepository assessmentRepository) {
        this.teamRepository = teamRepository;
        this.membershipRepository = membershipRepository;
        this.assessmentRepository = assessmentRepository;
    }

    public TeamEmailPreviewDto buildTeamPreview(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        List<TeamMembership> memberships =
                membershipRepository.findByTeam(team);

        List<Assessment> assessments =
                assessmentRepository.findByMembershipIn(memberships);

        // Team results
        List<CriterionResultDto> teamResults = new ArrayList<>();
        double teamTotal = 0;

        // Student results
        Map<Student, List<CriterionResultDto>> studentMap = new HashMap<>();
        Map<Student, Double> studentTotals = new HashMap<>();

        for (Assessment a : assessments) {
            Criterion c = a.getCriterion();
            TeamMembership m = a.getTeamMembership();

            CriterionResultDto dto = new CriterionResultDto(
                    c.getCategory(),
                    c.getName(),
                    a.getPoints(),
                    c.getMaxPoints(),
                    a.getFeedback()
            );

            if (c.getEvaluationMethod() == EvaluationMethod.PER_TEAM) {
                teamResults.add(dto);
                teamTotal += a.getPoints();
            } else {
                Student s = m.getStudent();
                studentMap.computeIfAbsent(s, k -> new ArrayList<>()).add(dto);
                studentTotals.merge(s, a.getPoints(), Double::sum);
            }
        }

        // Build student DTOs
        List<StudentPreviewDto> students = new ArrayList<>();

        for (Student s : studentMap.keySet()) {
            students.add(new StudentPreviewDto(s.getFirstName() + " " + s.getLastName(),
                    s.getEmail(), studentMap.get(s), studentTotals.getOrDefault(s, 0.0)));
        }

        Activity activity = memberships.get(0).getActivity();

        return new TeamEmailPreviewDto(activity.getCourse().getName(), activity.getName(), team.getName(),
                teamResults, students, teamTotal);
    }

    public TeamEmailPreviewsDto buildTeamEmailPreviews(Long teamId) {

        TeamEmailPreviewDto teamPreview = buildTeamPreview(teamId);

        List<EmailPreviewDto> emails = new ArrayList<>();

        for (StudentPreviewDto student : teamPreview.getStudentResults()) {

            String subject = teamPreview.getCourse()
                    + " – " + teamPreview.getActivity()
                    + " – Evaluation results";

            String body = buildEmailBody(teamPreview, student);

            List<CriterionResultDto> allCriteria = new ArrayList<>();
            allCriteria.addAll(teamPreview.getTeamResults());
            allCriteria.addAll(student.getResults());

            emails.add(new EmailPreviewDto(
                    student.getFullName(),
                    student.getEmail(),
                    subject,
                    body,
                    student.getResults(),
                    teamPreview.getTeamTotal(),
                    student.getTotal()
            ));
        }

        return new TeamEmailPreviewsDto(
                teamPreview.getCourse(),
                teamPreview.getActivity(),
                teamPreview.getTeam(),
                emails
        );
    }

    private String buildEmailBody(TeamEmailPreviewDto team, StudentPreviewDto student) {
        StringBuilder sb = new StringBuilder();

        sb.append("Dear ").append(student.getFullName()).append(",\n\n");

        sb.append("The evaluation for the activity \"")
                .append(team.getActivity())
                .append("\" in the course ")
                .append(team.getCourse())
                .append(" has been completed.\n\n");

        sb.append("Team: ").append(team.getTeam()).append("\n\n");

        sb.append("Team evaluation:\n");
        for (CriterionResultDto r : team.getTeamResults()) {
            sb.append("- ")
                    .append(r.getName())
                    .append(": ")
                    .append(r.getPoints())
                    .append("/")
                    .append(r.getMaxPoints())
                    .append("\n");
        }

        sb.append("Team total: ")
                .append(team.getTeamTotal())
                .append("\n\n");

        sb.append("Your individual evaluation:\n");
        for (CriterionResultDto r : student.getResults()) {
            sb.append("- ")
                    .append(r.getName())
                    .append(": ")
                    .append(r.getPoints())
                    .append("/")
                    .append(r.getMaxPoints())
                    .append("\n");
        }

        sb.append("Your total: ")
                .append(student.getTotal())
                .append("\n\n");

        sb.append("Your final total: ")
                .append(student.getTotal() + team.getTeamTotal())
                .append("\n\n");

        sb.append("Best regards,\nThe teaching team");

        return sb.toString();
    }
}
