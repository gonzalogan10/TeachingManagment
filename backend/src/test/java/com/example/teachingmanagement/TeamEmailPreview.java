package com.example.teachingmanagement;

import com.example.teachingmanagement.dto.email.CriterionResultDto;
import com.example.teachingmanagement.dto.email.StudentPreviewDto;
import com.example.teachingmanagement.dto.email.TeamEmailPreviewDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TeamEmailPreviewTest {

    @Test
    void createTeamEmailPreviewDto() {
        CriterionResultDto teamResult =
                new CriterionResultDto("Documentation", "Structure", 18, 20, "Well organized");

        StudentPreviewDto student =
                new StudentPreviewDto(
                        "Alice Smith",
                        "alice@fit.cvut.cz",
                        List.of(teamResult),
                        7
                );

        TeamEmailPreviewDto dto =
                new TeamEmailPreviewDto(
                        "IVS",
                        "Project 1",
                        "Team A",
                        List.of(teamResult),
                        List.of(student),
                        18
                );

        assertThat(dto.getCourse()).isEqualTo("IVS");
        assertThat(dto.getActivity()).isEqualTo("Project 1");
        assertThat(dto.getTeam()).isEqualTo("Team A");
        assertThat(dto.getTeamResults()).hasSize(1);
        assertThat(dto.getStudentResults()).hasSize(1);
        assertThat(dto.getTeamTotal()).isEqualTo(18);
    }
}