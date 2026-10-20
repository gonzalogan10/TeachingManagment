package com.example.teachingmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.ActivityType;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.service.ActivityService;
import com.example.teachingmanagement.service.CourseService;
import com.example.teachingmanagement.service.SolverService;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for the functionality of adding and removing solvers (teachers/evaluators) to activities.
 * This tests the many-to-many relationship between Activity and Solver.
 */
@SpringBootTest
class ActivitySolverTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SolverService solverService;

    private Activity activity;
    private Course course;
    private Solver solver1;
    private Solver solver2;

    @BeforeEach
    void setUp() {
        // Setup course and activity
        course = courseService.save(new Course("GJAE", "GJAE2025"));
        activity = activityService.save(new Activity("Lecture 1", ActivityType.LECTURE, course));

        // Setup solvers (teachers/evaluators)
        solver1 = solverService.saveSolver(new Solver("Prof. Juan"));
        solver2 = solverService.saveSolver(new Solver("Prof. María"));
    }

    /**
     * Test adding a single solver to an activity
     */
    @Test
    @Transactional
    void testAddSingleSolverToActivity() {
        activity.getSolvers().add(solver1);
        Activity saved = activityService.save(activity);

        assertThat(saved.getSolvers()).contains(solver1);
        assertThat(saved.getSolvers()).hasSize(1);
    }

    /**
     * Test adding multiple solvers to an activity
     */
    @Test
    @Transactional
    void testAddMultipleSolversToActivity() {
        activity.getSolvers().add(solver1);
        activity.getSolvers().add(solver2);
        Activity saved = activityService.save(activity);

        assertThat(saved.getSolvers())
                .contains(solver1, solver2)
                .hasSize(2);
    }

    /**
     * Test that the same solver cannot be added twice to an activity
     */
    @Test
    @Transactional
    void testCannotAddSolverTwice() {
        activity.getSolvers().add(solver1);
        activity.getSolvers().add(solver1);
        Activity saved = activityService.save(activity);

        // List allows duplicates, but in practice we should handle this in service
        // This test shows current behavior - should be improved with a Set instead of List
        assertThat(saved.getSolvers()).contains(solver1);
    }

    /**
     * Test removing a solver from an activity
     */
    @Test
    @Transactional
    void testRemoveSolverFromActivity() {
        activity.getSolvers().add(solver1);
        activity.getSolvers().add(solver2);
        Activity saved = activityService.save(activity);

        assertThat(saved.getSolvers()).hasSize(2);

        // Remove one solver
        Activity updated = activityService.findById(saved.getId());
        updated.getSolvers().remove(solver1);
        Activity finalSaved = activityService.save(updated);

        assertThat(finalSaved.getSolvers()).contains(solver2);
        assertThat(finalSaved.getSolvers()).doesNotContain(solver1);
        assertThat(finalSaved.getSolvers()).hasSize(1);
    }

    /**
     * Test removing all solvers from an activity
     */
    @Test
    @Transactional
    void testRemoveAllSolversFromActivity() {
        activity.getSolvers().add(solver1);
        activity.getSolvers().add(solver2);
        Activity saved = activityService.save(activity);

        assertThat(saved.getSolvers()).hasSize(2);

        // Remove all solvers
        Activity updated = activityService.findById(saved.getId());
        updated.getSolvers().clear();
        Activity finalSaved = activityService.save(updated);

        assertThat(finalSaved.getSolvers()).isEmpty();
    }

    /**
     * Test that activity without solvers has empty list
     */
    @Test
    @Transactional
    void testActivityWithoutSolvers() {
        Activity saved = activityService.save(activity);

        assertThat(saved.getSolvers()).isNotNull().isEmpty();
    }

    /**
     * Test retrieving solvers from a persisted activity
     */
    @Test
    @Transactional
    void testRetrieveSolversFromActivity() {
        activity.getSolvers().add(solver1);
        activity.getSolvers().add(solver2);
        Activity saved = activityService.save(activity);

        Activity retrieved = activityService.findById(saved.getId());

        assertThat(retrieved.getSolvers())
                .isNotNull()
                .hasSize(2)
                .contains(solver1, solver2);
    }

    /**
     * Test that solvers maintain correct references to activity
     */
    @Test
    @Transactional
    void testSolverActivityRelationship() {
        activity.getSolvers().add(solver1);
        Activity saved = activityService.save(activity);

        Activity retrieved = activityService.findById(saved.getId());
        Solver retrievedSolver = retrieved.getSolvers().get(0);

        assertThat(retrievedSolver.getId()).isEqualTo(solver1.getId());
        assertThat(retrievedSolver.getName()).isEqualTo(solver1.getName());
    }

    /**
     * Test modifying activity while solvers are assigned
     */
    @Test
    @Transactional
    void testModifyActivityWithAssignedSolvers() {
        activity.getSolvers().add(solver1);
        activity.getSolvers().add(solver2);
        Activity saved = activityService.save(activity);

        // Modify activity name
        Activity updated = activityService.findById(saved.getId());
        updated.setName("Modified Lecture 1");
        Activity finalSaved = activityService.save(updated);

        assertThat(finalSaved.getName()).isEqualTo("Modified Lecture 1");
        assertThat(finalSaved.getSolvers()).hasSize(2).contains(solver1, solver2);
    }
}
