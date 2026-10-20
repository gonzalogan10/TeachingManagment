package com.example.teachingmanagement.repository;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.Student;
import com.example.teachingmanagement.model.Team;
import com.example.teachingmanagement.model.TeamMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMembershipRepository
        extends JpaRepository<TeamMembership, Long> {

    // Rule enforcement: one team per student per activity
    boolean existsByStudentAndActivity(Student student, Activity activity);

    // Fetch membership for a student in a given activity
    Optional<TeamMembership> findByStudentAndActivity(Student student, Activity activity);

    // Returns all memberships for a student (all courses/projects)
    List<TeamMembership> findByStudent(Student student);

    // Returns all memberships for a team
    List<TeamMembership> findByTeam(Team team);

    // Returns all memberships for an activity
    List<TeamMembership> findByActivity(Activity activity);
}