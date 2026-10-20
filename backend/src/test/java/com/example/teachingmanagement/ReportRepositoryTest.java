package com.example.teachingmanagement;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.teachingmanagement.model.Activity;
import com.example.teachingmanagement.model.ActivityType;
import com.example.teachingmanagement.model.Course;
import com.example.teachingmanagement.model.Report;
import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.repository.ActivityRepository;
import com.example.teachingmanagement.repository.CourseRepository;
import com.example.teachingmanagement.repository.ReportRepository;
import com.example.teachingmanagement.repository.SolverRepository;
import com.example.teachingmanagement.repository.TaskRepository;

@DataJpaTest
class ReportRepositoryTest {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private SolverRepository solverRepo;

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private CourseRepository courseRepo;

@Autowired
    private ActivityRepository activityRepo;

    private Solver solver;
    private Task task;
    private Course course;
    private Activity activity;

    @BeforeEach
    void setUp() {
        course = courseRepo.save(new Course("GJAE", "GJAE2025"));
        activity = activityRepo.save(new Activity("Midterm", ActivityType.MIDTERM, course));
        task = taskRepo.save(new Task("Assessment", activity));
        solver = solverRepo.save(new Solver("Sara"));

    }

    @Test
    void testCreateReport() {
        Report report = new Report("Midterm test assessment", "2 hours", 1.0/6, 100, solver, task);
        Report saved = reportRepo.save(report);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getContent()).isEqualTo("Midterm test assessment");
    }

    @Test
    void findReportById() {
        Report report = new Report("Midterm test assessment", "2 hours", 1.0/6, 100, solver, task);
        Report saved = reportRepo.save(report);

        Optional<Report> found = reportRepo.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found).hasValueSatisfying(r -> assertThat(r.getContent()).isEqualTo("Midterm test assessment"));
    }

    @Test
    void findAllReports() {
        reportRepo.save(new Report("Report 1", "2 hours", 1.0/6, 100, solver, task));
        reportRepo.save(new Report("Report 2", "3 hours", 1.0/4, 50, solver, task));

        List<Report> reports = reportRepo.findAll();
        assertThat(reports).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void updateReport() {
        Report report = new Report("Midterm test assessment", "2 hours", 1.0/6, 100, solver, task);
        Report saved = reportRepo.save(report);

        saved.setContent("Updated assessment");
        Report updated = reportRepo.save(saved);

        assertThat(updated.getContent()).isEqualTo("Updated assessment");
    }

    @Test
    void deleteReport() {
        Report report = new Report("Midterm test assessment", "2 hours", 1.0/6, 100, solver, task);
        Report saved = reportRepo.save(report);

        reportRepo.deleteById(saved.getId());

        Optional<Report> found = reportRepo.findById(saved.getId());
        assertThat(found).isNotPresent();
    }
}