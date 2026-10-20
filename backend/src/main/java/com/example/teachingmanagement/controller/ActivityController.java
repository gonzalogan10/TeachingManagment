package com.example.teachingmanagement.controller;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.service.ActivityService;
import com.example.teachingmanagement.service.CourseService;
import com.example.teachingmanagement.service.SolverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService service;
    private final SolverService solverService;
    private final CourseService courseService;

    public ActivityController(ActivityService service, SolverService solverService, CourseService courseService) {
        this.service = service;
        this.solverService = solverService;
        this.courseService = courseService;
    }

    @PostMapping
    public Activity create(@RequestBody Activity activity) {
        Long courseId = activity.getCourse().getId();
        Course course = courseService.findById(courseId);
        activity.setCourse(course);
        return service.save(activity);
    }

    @GetMapping
    public List<Activity> getAll() {
        return service.findAll();
    }

    @GetMapping("/course/{courseId}")
    public List<Activity> getActivitiesByCourse(@PathVariable Long courseId) {
        return service.findByCourseId(courseId);
    }

    /**
     * Create an activity for a specific course
     * PathVariable is used to extract values from the URL path and convert them into Long parameters.
     * @param courseId ID of the course for which the activity will be created
     * @param activity Activity to be created
     * @return
     */
    @PostMapping("/course/{courseId}")
    public Activity createActivityForCourse(@PathVariable Long courseId, @RequestBody Activity activity) {
        try {
            if (activity.getName() == null || activity.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Activity name is required");
            }
            if (activity.getType() == null) {
                throw new IllegalArgumentException("Activity type is required");
            }
            return service.createActivityForCourse(courseId, activity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating activity: " + e.getMessage(), e);
        }
    }

    /**
     * Add a solver (teacher) to an activity for evaluation
     * @param activityId ID of the activity
     * @param solverId ID of the solver
     * @return the updated activity
     */
    @PostMapping("/{activityId}/solvers/{solverId}")
    public Activity addSolverToActivity(@PathVariable Long activityId, @PathVariable Long solverId) {
        Activity activity = service.findById(activityId);
        Solver solver = solverService.findById(solverId);
        activity.getSolvers().add(solver);
        return service.save(activity);
    }

    /**
     * Remove a solver from an activity
     * @param activityId ID of the activity
     * @param solverId ID of the solver
     * @return the updated activity
     */
    @DeleteMapping("/{activityId}/solvers/{solverId}")
    public Activity removeSolverFromActivity(@PathVariable Long activityId, @PathVariable Long solverId) {
        Activity activity = service.findById(activityId);
        Solver solver = solverService.findById(solverId);
        activity.getSolvers().remove(solver);
        return service.save(activity);
    }

    /**
     * Get all solvers assigned to an activity
     * @param activityId ID of the activity
     * @return list of solvers assigned to the activity
     */
    @GetMapping("/{activityId}/solvers")
    public List<Solver> getSolversForActivity(@PathVariable Long activityId) {
        return service.findById(activityId).getSolvers();
    }

    /**
     * Delete an activity by ID
     * @param activityId ID of the activity to delete
     */
    @DeleteMapping("/{activityId}")
    public void deleteActivity(@PathVariable Long activityId) {
        service.deleteActivity(activityId);
    }
}
