package com.example.teachingmanagement;

import com.example.teachingmanagement.dto.email.StudentPreviewDto;
import com.example.teachingmanagement.dto.email.TeamEmailPreviewDto;
import com.example.teachingmanagement.model.*;
import com.example.teachingmanagement.repository.AssessmentRepository;
import com.example.teachingmanagement.repository.TeamMembershipRepository;
import com.example.teachingmanagement.repository.TeamRepository;

import com.example.teachingmanagement.service.email.EmailPreviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailPreviewServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMembershipRepository membershipRepository;

    @Mock
    private AssessmentRepository assessmentRepository;

    @InjectMocks
    private EmailPreviewService emailPreviewService;

    private Team team;
    private Activity activity;
    private TeamMembership mAlice;
    private TeamMembership mBob;

    @BeforeEach
    void setUp() {
        // Course & Activity
        Course course = new Course("IVS", "IVS2025");
        activity = new Activity("Project 1", ActivityType.PROJECT, course);

        team = new Team("Team A");

        Student alice = new Student("Alice", "Smith", "alice@fit.cvut.cz");
        Student bob = new Student("Bob", "Jones", "bob@fit.cvut.cz");

        mAlice = new TeamMembership(alice, team, activity);
        mBob = new TeamMembership(bob, team, activity);

        team.setMemberships(List.of(mAlice, mBob));

        Criterion teamCriterion = new Criterion(
                "Documentation",
                "Structure",
                null,
                EvaluationMethod.PER_TEAM,
                20,
                false,
                null
        );

        Criterion studentCriterion = new Criterion(
                "Contribution",
                "Activity",
                null,
                EvaluationMethod.PER_STUDENT,
                10,
                false,
                null
        );


        Assessment teamAssessment = new Assessment(teamCriterion, 18.0, "Well organized");
        teamAssessment.setTeamMembership(mAlice);

        Assessment aliceAssessment = new Assessment(studentCriterion, 7.0, "Good effort");
        aliceAssessment.setTeamMembership(mAlice);

        Assessment bobAssessment = new Assessment(studentCriterion, 9.0, "Excellent");
        bobAssessment.setTeamMembership(mBob);


        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(membershipRepository.findByTeam(team)).thenReturn(List.of(mAlice, mBob));
        when(assessmentRepository.findByMembershipIn(List.of(mAlice, mBob)))
                .thenReturn(List.of(teamAssessment, aliceAssessment, bobAssessment));
    }

    @Test
    void buildTeamPreview_success() {
        TeamEmailPreviewDto dto = emailPreviewService.buildTeamPreview(1L);

        assertThat(dto.getCourse()).isEqualTo("IVS");
        assertThat(dto.getActivity()).isEqualTo("Project 1");
        assertThat(dto.getTeam()).isEqualTo("Team A");

        assertThat(dto.getTeamResults()).hasSize(1);
        assertThat(dto.getTeamTotal()).isEqualTo(18.0);

        assertThat(dto.getStudentResults()).hasSize(2);
        assertThat(dto.getStudentResults())
                .extracting(StudentPreviewDto::getEmail)
                .containsExactlyInAnyOrder(
                        "alice@fit.cvut.cz",
                        "bob@fit.cvut.cz"
                );
    }
}