package com.example.teachingmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.ActivityType;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.repository.TaskRepository;
import com.example.teachingmanagement.service.ActivityService;
import com.example.teachingmanagement.service.CourseService;
import com.example.teachingmanagement.service.SolverService;

import jakarta.transaction.Transactional;

@SpringBootTest
class SolverServiceTest {

    @Autowired
    private SolverService solverService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ActivityService activityService;

    private Task task;
    private Solver solver;
    private Course course;
    private Activity activity;

    @BeforeEach
    void setUp() {
        course = courseService.save(new Course("GJAE", "GJAE2025"));
        activity = activityService.save(new Activity("Lecture", ActivityType.LECTURE, course));
        task = taskRepository.save(new Task("Test Task", activity));
        solver = new Solver ("Test Solver");
    }

    @Test
    void saveSolver() {
        Solver saved = solverService.saveSolver(solver);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findAll() {
        assertThat(solverService.findAllSolvers()).isNotNull();
    }

    @Test
    void findById() {
        Solver saved = solverService.saveSolver(solver);
        Solver found = solverService.findById(saved.getId());
        assertThat(found).isNotNull();
    }

    @Test
    @Transactional //keeps sesion open to fetch lazy collection, so that we can verify the solvers
    void addSolverToTask() {
        solverService.saveSolver(solver);
        solverService.addSolverToTask(task.getId(), solver.getId());

        //verifies that the solver has been added to the task
        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(updatedTask.getSolvers()).contains(solver);
        assertThat(updatedTask.getSolvers()).hasSize(1);

        // cannot add twice
        solverService.addSolverToTask(task.getId(), solver.getId());
        Task updatedTask2 = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(updatedTask2.getSolvers()).hasSize(1);
    }

    @Test
    @Transactional //keeps sesion open to fetch lazy collection, so that we can verify the solvers
    void setMainSolver() {
        solverService.saveSolver(solver);
        solverService.setMainSolver(solver.getId(), task.getId());

        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(updatedTask.getMainSolver()).isEqualTo(solver);
    }
}
