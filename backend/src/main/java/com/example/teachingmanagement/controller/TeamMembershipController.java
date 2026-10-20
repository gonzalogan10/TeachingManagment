package com.example.teachingmanagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.teachingmanagement.dto.StudentDTO;
import com.example.teachingmanagement.dto.TeamDTO;
import com.example.teachingmanagement.dto.TeamMembershipDTO;
import com.example.teachingmanagement.model.Student;
import com.example.teachingmanagement.model.Team;
import com.example.teachingmanagement.service.StudentService;
import com.example.teachingmanagement.service.TeamMembershipService;
import com.example.teachingmanagement.service.TeamService;

@RestController
@RequestMapping("/api/team-memberships")
public class TeamMembershipController {

    private final TeamMembershipService service;
    private final TeamService teamService;
    private final StudentService studentService;

    public TeamMembershipController(TeamMembershipService service, TeamService teamService, StudentService studentService) {
        this.service = service;
        this.teamService = teamService;
        this.studentService = studentService;
    }

    private TeamDTO convertToDTO(Team team) {
        List<TeamMembershipDTO> membershipDTOs = team.getMemberships() != null ?
            team.getMemberships().stream()
                .map(m -> new TeamMembershipDTO(
                    m.getId(),
                    new StudentDTO(
                        m.getStudent().getId(),
                        m.getStudent().getFirstName(),
                        m.getStudent().getLastName(),
                        m.getStudent().getEmail()
                    )
                ))
                .collect(Collectors.toList()) : List.of();
        return new TeamDTO(team.getId(), team.getName(), membershipDTOs);
    }

    /**
     * Add a student to a team
     * @param teamId
     * @param studentId
     * @return
     */
    @PostMapping("/team/{teamId}/student/{studentId}")
    public TeamDTO addStudentToTeam(@PathVariable Long teamId, @PathVariable Long studentId) {
        Team team = teamService.findById(teamId);
        Student student = studentService.findById(studentId);
        service.addStudentToTeam(student, team);
        return convertToDTO(teamService.findById(teamId)); // Return updated team with members
    }

    /**
     * Remove a membership
     * @param membershipId
     */
    @DeleteMapping("/{membershipId}")
    public void removeMembership(@PathVariable Long membershipId) {
        service.removeMembership(membershipId);
    }
}
