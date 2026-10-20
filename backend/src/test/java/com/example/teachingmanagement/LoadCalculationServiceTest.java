package com.example.teachingmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.ActivityType;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.model.Report;
import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.model.ActivityLoad;
import com.example.teachingmanagement.repository.ActivityRepository;
import com.example.teachingmanagement.repository.CourseRepository;
import com.example.teachingmanagement.repository.ReportRepository;
import com.example.teachingmanagement.repository.SolverRepository;
import com.example.teachingmanagement.repository.TaskRepository;
import com.example.teachingmanagement.service.LoadCalculationService;

import java.util.List;

@SpringBootTest
public class LoadCalculationServiceTest {

    @Autowired
    private LoadCalculationService service;

    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private ActivityRepository activityRepo;
    @Autowired
    private TaskRepository taskRepo;
    @Autowired
    private SolverRepository solverRepo;
    @Autowired
    private ReportRepository reportRepo;

    private Activity activity;

    @BeforeEach
    void setUp() {
        Course course = courseRepo.save(new Course("GJAE", "GJAE2025"));
        activity = activityRepo.save(new Activity("Midterm", ActivityType.MIDTERM, course));

        Task task = taskRepo.save(new Task("Correction", activity));
        Solver solver = solverRepo.save(new Solver("Sara"));

        reportRepo.save(new Report("R1", "2h", 1.0, 100, solver, task));
        reportRepo.save(new Report("R2", "3h", 0.5, 50, solver, task));
    }

    @Test
    void findReportsForActivity() {
        List<Report> reports = service.findReportsForActivity(activity.getId());

        assertThat(reports).isNotEmpty();
        assertThat(reports).hasSize(2);

        // Verify time is stored as string but can be parsed to double
        assertThat(reports.get(0).getTimeSpent()).isEqualTo("2h");
        assertThat(service.calculateTotalHours(List.of(reports.get(0)))).isEqualTo(2.0);
        assertThat(reports.get(0).getNumStudents()).isEqualTo(100);
        assertThat(reports.get(0).getProportion()).isEqualTo(1.0);

        assertThat(reports.get(1).getTimeSpent()).isEqualTo("3h");
        assertThat(service.calculateTotalHours(List.of(reports.get(1)))).isEqualTo(3.0);
        assertThat(reports.get(1).getNumStudents()).isEqualTo(50);
        assertThat(reports.get(1).getProportion()).isEqualTo(0.5);
    }

    @Test
    void calculateLoad() {
        ActivityLoad load = service.calculateLoad(activity.getId());

        assertThat(load.getTotalHours()).isEqualTo(5.0);
        assertThat(load.getTotalStudents()).isEqualTo(150);
        assertThat(load.getTotalProportion()).isEqualTo(1.5);
    }

    
}
