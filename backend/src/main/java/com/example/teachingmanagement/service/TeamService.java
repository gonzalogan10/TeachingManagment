package com.example.teachingmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.model.Team;
import com.example.teachingmanagement.repository.TeamRepository;


@Service
public class TeamService {

    private final TeamRepository repository;
    private final CourseService courseService;

    public TeamService(TeamRepository repository, CourseService courseService) {
        this.repository = repository;
        this.courseService = courseService;
    }

    public Team save(Team team) {
        return repository.save(team);
    }

    public List<Team> findAll() {
        return repository.findAll();
    }

    public Team findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    public List<Team> findByCourseId(Long courseId) {
        return repository.findByCourseId(courseId);
    }

    public Team createTeamForCourse(Long courseId, Team team) {
        Course course = courseService.findById(courseId);
        team.setCourse(course);
        return repository.save(team);
    }

    public void deleteTeam(Long id) {
        repository.deleteById(id);
    }
}
