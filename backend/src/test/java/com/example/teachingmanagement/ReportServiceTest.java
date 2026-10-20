package com.example.teachingmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.ActivityType;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.model.Report;
import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.service.ActivityService;
import com.example.teachingmanagement.service.CourseService;
import com.example.teachingmanagement.service.ReportService;
import com.example.teachingmanagement.service.SolverService;
import com.example.teachingmanagement.service.TaskService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SolverService solverService;

    private Course course;
    private Activity activity;
    private Task task;
    private Solver solver;

    @BeforeEach
    void setUp() {
        course = courseService.save(new Course("GJAE", "GJAE2025"));
        activity = activityService.createActivityForCourse(course.getId(), new Activity("Project", ActivityType.PROJECT, course));
        task = taskService.createTaskForActivity(activity.getId(), new Task("Implementation", activity));
        solver = solverService.saveSolver(new Solver("Test solver"));

    }

    @Test
    void saveReport() {
        Report report = new Report("Test report", "1 hour", 0.1, 10, solver, task);
        Report saved = reportService.save(report);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void createForActivity() {
        Task saved = taskService.createTaskForActivity(activity.getId(), task);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getActivity().getId()).isEqualTo(activity.getId());
    }

    @Test
    void findAll() {
        assertThat(reportService.findAll()).isNotNull();
    }

    @Test
    void findBySolver() {
        Report report = new Report("Solver report", "2 hours", 0.2, 20, solver, task);
        reportService.save(report);

        assertThat(reportService.findBySolver(solver.getId())).isNotEmpty();
    }

    @Test
    void findByTask() {
        Report report = new Report("Task report", "3 hours", 0.3, 30, solver, task);
        reportService.save(report);

        assertThat(reportService.findByTask(task.getId())).isNotEmpty();
    }   
}