package com.example.teachingmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.Student;
import com.example.teachingmanagement.model.Team;
import com.example.teachingmanagement.model.TeamMembership;
import com.example.teachingmanagement.repository.TeamMembershipRepository;

@Service
public class TeamMembershipService {

    private final TeamMembershipRepository repository;

    public TeamMembershipService(TeamMembershipRepository repository) {
        this.repository = repository;
    }

    public TeamMembership addStudentToTeam(Student student, Team team, Activity activity) {
        boolean alreadyExists = repository.existsByStudentAndActivity(student, activity);
        if (alreadyExists) {
            throw new IllegalStateException("Student is already in a team for this activity");
        }
        TeamMembership membership = new TeamMembership(student, team, activity);
        return repository.save(membership);
    }

    public TeamMembership addStudentToTeam(Student student, Team team) {
        // Check if student is already in this team
        boolean alreadyExists = repository.findByTeam(team).stream()
                .anyMatch(m -> m.getStudent().getId().equals(student.getId()));
        if (alreadyExists) {
            throw new IllegalStateException("Student is already a member of this team");
        }
        TeamMembership membership = new TeamMembership(student, team);
        return repository.save(membership);
    }

    public TeamMembership findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeamMembership not found"));
    }

    public List<TeamMembership> findByTeam(Team team) {
        return repository.findByTeam(team);
    }

    public List<TeamMembership> findByStudent(Student student) {
        return repository.findByStudent(student);
    }

    public void removeMembership(Long id) {
        repository.deleteById(id);
    }
}
