package com.example.teachingmanagement.service;

import com.example.teachingmanagement.dto.AssessmentDetailDto;
import com.example.teachingmanagement.model.Assessment;
import com.example.teachingmanagement.model.Criterion;
import com.example.teachingmanagement.model.Team;
import com.example.teachingmanagement.model.TeamMembership;
import com.example.teachingmanagement.repository.AssessmentRepository;
import com.example.teachingmanagement.repository.CriterionRepository;
import com.example.teachingmanagement.repository.TeamMembershipRepository;
import com.example.teachingmanagement.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    private final AssessmentRepository assessmentRepository;
    private final CriterionRepository criterionRepository;
    private final TeamRepository teamRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    public EvaluationService(AssessmentRepository assessmentRepository, 
                             CriterionRepository criterionRepository,
                             TeamRepository teamRepository,
                             TeamMembershipRepository teamMembershipRepository) {
        this.assessmentRepository = assessmentRepository;
        this.criterionRepository = criterionRepository;
        this.teamRepository = teamRepository;
        this.teamMembershipRepository = teamMembershipRepository;
    }

    public Assessment createEvaluation(Long teamId, Long criterionId, Long activityId, Double points, String feedback) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        
        Criterion criterion = criterionRepository.findById(criterionId)
                .orElseThrow(() -> new RuntimeException("Criterion not found"));

        // Check if this criterion is already evaluated for this team IN THIS ACTIVITY
        if (assessmentRepository.existsByCriterionAndTeamAndActivity(criterionId, teamId, activityId)) {
            throw new RuntimeException("This criterion has already been evaluated for this team in this activity");
        }

        // For team-based evaluation, we create assessments for each team member
        List<TeamMembership> memberships = teamMembershipRepository.findByTeam(team);
        
        if (memberships.isEmpty()) {
            throw new RuntimeException("Team has no members");
        }

        // Each team member gets the full points assigned to the team
        Assessment lastAssessment = null;
        for (TeamMembership membership : memberships) {
            Assessment assessment = new Assessment();
            assessment.setCriterion(criterion);
            assessment.setTeamMembership(membership);
            assessment.setActivityId(activityId);
            assessment.setPoints(points);
            assessment.setFeedback(feedback);
            lastAssessment = assessmentRepository.save(assessment);
        }

        // Return the last assessment that was saved
        return lastAssessment;
    }
    
    public Assessment createEvaluationPerStudent(Long teamId, Long criterionId, Long activityId, Map<Long, Double> studentPoints, String feedback) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        
        Criterion criterion = criterionRepository.findById(criterionId)
                .orElseThrow(() -> new RuntimeException("Criterion not found"));

        // Check if this criterion is already evaluated for this team IN THIS ACTIVITY
        if (assessmentRepository.existsByCriterionAndTeamAndActivity(criterionId, teamId, activityId)) {
            throw new RuntimeException("This criterion has already been evaluated for this team in this activity");
        }

        // For team-based evaluation, we create assessments for each team member
        List<TeamMembership> memberships = teamMembershipRepository.findByTeam(team);
        
        if (memberships.isEmpty()) {
            throw new RuntimeException("Team has no members");
        }

        // Each team member gets their individual points
        Assessment lastAssessment = null;
        for (TeamMembership membership : memberships) {
            Long studentId = membership.getStudent().getId();
            Double pointsForStudent = studentPoints.getOrDefault(studentId, 0.0);
            
            Assessment assessment = new Assessment();
            assessment.setCriterion(criterion);
            assessment.setTeamMembership(membership);
            assessment.setActivityId(activityId);
            assessment.setPoints(pointsForStudent);
            assessment.setFeedback(feedback);
            lastAssessment = assessmentRepository.save(assessment);
        }

        return lastAssessment;
    }

    public List<Assessment> getAssessmentsByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        
        return assessmentRepository.findByTeamIdWithDetails(teamId);
    }

    public Assessment updateEvaluation(Long assessmentId, Double points, String feedback) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        
        assessment.setPoints(points);
        assessment.setFeedback(feedback);
        return assessmentRepository.save(assessment);
    }

    public void updateEvaluationByCriterionAndTeam(Long criterionId, Long teamId, Double points, String feedback) {
        List<Assessment> assessments = assessmentRepository.findByCriterionAndTeam(criterionId, teamId);
        
        if (assessments.isEmpty()) {
            throw new RuntimeException("No evaluation found for this criterion and team");
        }
        
        // Update all assessments for this criterion-team pair
        for (Assessment assessment : assessments) {
            assessment.setPoints(points);
            assessment.setFeedback(feedback);
            assessmentRepository.save(assessment);
        }
    }
    
    public void updateEvaluationByCriterionAndTeamPerStudent(Long criterionId, Long teamId, Map<Long, Double> studentPoints, String feedback) {
        List<Assessment> assessments = assessmentRepository.findByCriterionAndTeam(criterionId, teamId);
        
        if (assessments.isEmpty()) {
            throw new RuntimeException("No evaluation found for this criterion and team");
        }
        
        // Update each assessment with the corresponding student's points
        for (Assessment assessment : assessments) {
            Long studentId = assessment.getTeamMembership().getStudent().getId();
            Double pointsForStudent = studentPoints.getOrDefault(studentId, 0.0);
            assessment.setPoints(pointsForStudent);
            assessment.setFeedback(feedback);
            assessmentRepository.save(assessment);
        }
    }

    public void deleteEvaluation(Long assessmentId) {
        assessmentRepository.deleteById(assessmentId);
    }

    public boolean existsEvaluation(Long criterionId, Long teamId) {
        return assessmentRepository.existsByCriterionAndTeam(criterionId, teamId);
    }

    public List<AssessmentDetailDto> getAssessmentsByTeamAsDto(Long teamId) {
        List<Assessment> assessments = getAssessmentsByTeam(teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        List<TeamMembership> memberships = teamMembershipRepository.findByTeam(team);
        int teamSize = memberships.size();
        
        return assessments.stream()
                .map(a -> new AssessmentDetailDto(
                    a.getId(),
                    a.getCriterion().getId(),
                    a.getCriterion().getName(),
                    a.getTeamMembership().getStudent().getId(),
                    a.getTeamMembership().getStudent().getFirstName() + " " + a.getTeamMembership().getStudent().getLastName(),
                    a.getPoints(),
                    a.getFeedback(),
                    teamSize
                ))
                .collect(Collectors.toList());
    }

    public List<AssessmentDetailDto> getAssessmentsByTeamAndActivityAsDto(Long teamId, Long activityId) {
        List<Assessment> assessments = assessmentRepository.findByTeamAndActivityWithDetails(teamId, activityId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        List<TeamMembership> memberships = teamMembershipRepository.findByTeam(team);
        int teamSize = memberships.size();
        
        return assessments.stream()
                .map(a -> new AssessmentDetailDto(
                    a.getId(),
                    a.getCriterion().getId(),
                    a.getCriterion().getName(),
                    a.getTeamMembership().getStudent().getId(),
                    a.getTeamMembership().getStudent().getFirstName() + " " + a.getTeamMembership().getStudent().getLastName(),
                    a.getPoints(),
                    a.getFeedback(),
                    teamSize
                ))
                .collect(Collectors.toList());
    }

    public AssessmentDetailDto getEvaluationByCriterionAndTeam(Long criterionId, Long teamId) {
        List<Assessment> assessments = assessmentRepository.findByCriterionAndTeam(criterionId, teamId);
        
        if (assessments.isEmpty()) {
            return null;
        }
        
        // Get the first assessment (they all have the same points/feedback for the team)
        Assessment assessment = assessments.get(0);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        List<TeamMembership> memberships = teamMembershipRepository.findByTeam(team);
        
        return new AssessmentDetailDto(
            assessment.getId(),
            assessment.getCriterion().getId(),
            assessment.getCriterion().getName(),
            assessment.getTeamMembership().getStudent().getId(),
            assessment.getTeamMembership().getStudent().getFirstName() + " " + assessment.getTeamMembership().getStudent().getLastName(),
            assessment.getPoints(),
            assessment.getFeedback(),
            memberships.size()
        );
    }
    
    public List<AssessmentDetailDto> getAllEvaluationByCriterionAndTeam(Long criterionId, Long teamId) {
        List<Assessment> assessments = assessmentRepository.findByCriterionAndTeam(criterionId, teamId);
        
        if (assessments.isEmpty()) {
            return List.of();
        }
        
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        List<TeamMembership> memberships = teamMembershipRepository.findByTeam(team);
        
        return assessments.stream()
                .map(assessment -> new AssessmentDetailDto(
                    assessment.getId(),
                    assessment.getCriterion().getId(),
                    assessment.getCriterion().getName(),
                    assessment.getTeamMembership().getStudent().getId(),
                    assessment.getTeamMembership().getStudent().getFirstName() + " " + assessment.getTeamMembership().getStudent().getLastName(),
                    assessment.getPoints(),
                    assessment.getFeedback(),
                    memberships.size()
                ))
                .collect(Collectors.toList());
    }

    public List<AssessmentDetailDto> getAllEvaluationByCriterionAndTeamAndActivity(Long criterionId, Long teamId, Long activityId) {
        List<Assessment> assessments = assessmentRepository.findByCriterionAndTeamAndActivity(criterionId, teamId, activityId);
        
        if (assessments.isEmpty()) {
            return List.of();
        }
        
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        List<TeamMembership> memberships = teamMembershipRepository.findByTeam(team);
        
        return assessments.stream()
                .map(assessment -> new AssessmentDetailDto(
                    assessment.getId(),
                    assessment.getCriterion().getId(),
                    assessment.getCriterion().getName(),
                    assessment.getTeamMembership().getStudent().getId(),
                    assessment.getTeamMembership().getStudent().getFirstName() + " " + assessment.getTeamMembership().getStudent().getLastName(),
                    assessment.getPoints(),
                    assessment.getFeedback(),
                    memberships.size()
                ))
                .collect(Collectors.toList());
    }
}