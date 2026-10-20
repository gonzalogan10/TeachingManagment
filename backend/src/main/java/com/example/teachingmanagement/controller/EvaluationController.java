package com.example.teachingmanagement.controller;

import com.example.teachingmanagement.dto.AssessmentDetailDto;
import com.example.teachingmanagement.dto.EvaluationDto;
import com.example.teachingmanagement.model.Assessment;
import com.example.teachingmanagement.service.EvaluationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<?> createEvaluation(@RequestBody EvaluationDto dto) {
        try {
            System.out.println("=== EvaluationDto received ===");
            System.out.println("TeamId: " + dto.getTeamId());
            System.out.println("CriterionId: " + dto.getCriterionId());
            System.out.println("ActivityId: " + dto.getActivityId());
            System.out.println("Points (singlePoints): " + dto.getSinglePoints());
            System.out.println("StudentPoints: " + dto.getStudentPoints());
            System.out.println("IsPerStudent: " + dto.isPerStudent());
            System.out.println("Feedback: " + dto.getFeedback());
            System.out.println("======================");
            
            // Check if points is a Map (per-student) or a single value
            if (dto.isPerStudent()) {
                // Per-student evaluation
                Assessment result = evaluationService.createEvaluationPerStudent(
                    dto.getTeamId(),
                    dto.getCriterionId(),
                    dto.getActivityId(),
                    dto.getStudentPoints(),
                    dto.getFeedback() != null ? dto.getFeedback() : ""
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            } else {
                // Single points for all team members
                Double points = dto.getSinglePoints() != null ? dto.getSinglePoints() : 0.0;
                Assessment result = evaluationService.createEvaluation(
                    dto.getTeamId(),
                    dto.getCriterionId(),
                    dto.getActivityId(),
                    points,
                    dto.getFeedback() != null ? dto.getFeedback() : ""
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/team/{teamId}")
    public List<AssessmentDetailDto> getTeamAssessments(@PathVariable Long teamId) {
        return evaluationService.getAssessmentsByTeamAsDto(teamId);
    }

    @GetMapping("/team/{teamId}/activity/{activityId}")
    public List<AssessmentDetailDto> getTeamAssessmentsByActivity(@PathVariable Long teamId, @PathVariable Long activityId) {
        return evaluationService.getAssessmentsByTeamAndActivityAsDto(teamId, activityId);
    }

    @PutMapping("/{assessmentId}")
    public ResponseEntity<?> updateEvaluation(@PathVariable Long assessmentId,
                                      @RequestBody EvaluationDto dto) {
        try {
            Double points = dto.getSinglePoints() != null ? dto.getSinglePoints() : 0.0;
            Assessment result = evaluationService.updateEvaluation(
                assessmentId, 
                points, 
                dto.getFeedback() != null ? dto.getFeedback() : ""
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/criterion/{criterionId}/team/{teamId}")
    public ResponseEntity<?> updateEvaluationByCriterionAndTeam(@PathVariable Long criterionId,
                                                                 @PathVariable Long teamId,
                                                                 @RequestBody EvaluationDto dto) {
        try {
            // Check if points is a Map (per-student) or a single value
            if (dto.isPerStudent()) {
                // Per-student evaluation
                evaluationService.updateEvaluationByCriterionAndTeamPerStudent(
                    criterionId,
                    teamId,
                    dto.getStudentPoints(),
                    dto.getFeedback() != null ? dto.getFeedback() : ""
                );
            } else {
                // Single points for all team members
                Double points = dto.getSinglePoints() != null ? dto.getSinglePoints() : 0.0;
                evaluationService.updateEvaluationByCriterionAndTeam(
                    criterionId,
                    teamId,
                    points,
                    dto.getFeedback() != null ? dto.getFeedback() : ""
                );
            }
            return ResponseEntity.ok("✓ Updated all assessments for this criterion");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{assessmentId}")
    public void deleteEvaluation(@PathVariable Long assessmentId) {
        evaluationService.deleteEvaluation(assessmentId);
    }

    @GetMapping("/criterion/{criterionId}/team/{teamId}")
    public ResponseEntity<?> getEvaluationByCriterionAndTeam(@PathVariable Long criterionId, @PathVariable Long teamId) {
        try {
            // Return all assessments for this criterion-team pair (one per student)
            List<AssessmentDetailDto> assessments = evaluationService.getAllEvaluationByCriterionAndTeam(criterionId, teamId);
            if (!assessments.isEmpty()) {
                return ResponseEntity.ok(assessments);
            } else {
                return ResponseEntity.ok(new boolean[]{false});
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new boolean[]{false});
        }
    }

    @GetMapping("/criterion/{criterionId}/team/{teamId}/activity/{activityId}")
    public ResponseEntity<?> getEvaluationByCriterionAndTeamAndActivity(
            @PathVariable Long criterionId, 
            @PathVariable Long teamId,
            @PathVariable Long activityId) {
        try {
            // Return all assessments for this criterion-team-activity combination (one per student)
            List<AssessmentDetailDto> assessments = evaluationService.getAllEvaluationByCriterionAndTeamAndActivity(criterionId, teamId, activityId);
            if (!assessments.isEmpty()) {
                return ResponseEntity.ok(assessments);
            } else {
                return ResponseEntity.ok(new boolean[]{false});
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new boolean[]{false});
        }
    }
}
