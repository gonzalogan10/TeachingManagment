package com.example.teachingmanagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.teachingmanagement.dto.StudentDTO;
import com.example.teachingmanagement.dto.TeamDTO;
import com.example.teachingmanagement.dto.TeamMembershipDTO;
import com.example.teachingmanagement.model.Team;
import com.example.teachingmanagement.service.TeamService;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
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

    @PostMapping
    public TeamDTO create(@RequestBody Team team) {
        return convertToDTO(service.save(team));
    }

    @GetMapping
    public List<TeamDTO> getAll() {
        return service.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TeamDTO getById(@PathVariable Long id) {
        return convertToDTO(service.findById(id));
    }

    /**
     * Create a team for a specific course
     * @param courseId
     * @param team
     * @return
     */
    @PostMapping("/course/{courseId}")
    public TeamDTO createTeamForCourse(@PathVariable Long courseId, @RequestBody Team team) {
        return convertToDTO(service.createTeamForCourse(courseId, team));
    }

    /**
     * Get all teams for a specific course
     * @param courseId
     * @return
     */
    @GetMapping("/course/{courseId}")
    public List<TeamDTO> getTeamsByCourse(@PathVariable Long courseId) {
        return service.findByCourseId(courseId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        service.deleteTeam(id);
    }
}
