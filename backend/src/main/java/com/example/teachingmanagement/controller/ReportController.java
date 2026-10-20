package com.example.teachingmanagement.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.teachingmanagement.model.Report;
import com.example.teachingmanagement.service.ActivityService;
import com.example.teachingmanagement.service.CourseService;
import com.example.teachingmanagement.service.ReportService;
import com.example.teachingmanagement.service.SolverService;
import com.example.teachingmanagement.service.TaskService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService service;
    private CourseService courseService;
    private ActivityService activityService;
    private SolverService solverService;
    private TaskService taskService;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @PostMapping
    public Report create(@RequestBody Report report) {
        Long taskId = report.getTask().getId();
        report.setTask(taskService.findById(taskId));
        Long activityId = report.getTask().getActivity().getId();
        report.getTask().setActivity(activityService.findById(activityId));
        Long courseId = report.getTask().getActivity().getCourse().getId();
        report.getTask().getActivity().setCourse(courseService.findById(courseId));
        
        Long solverId = report.getSolver().getId();
        report.setSolver(solverService.findById(solverId));
        return service.save(report);
    }

    @GetMapping
    public List<Report> getAll() {
        return service.findAll();
    }

    /**
     * Create a report for a specific task and solver
     * PathVariable is used to extract values from the URL path and convert them into Long parameters
     * @param taskId
     * @param solverId
     * @param report
     * @return
     */
    @PostMapping("/task/{taskId}/solver/{solverId}")
    public Report createReportForTask(@PathVariable Long taskId, @PathVariable Long solverId, @RequestBody Report report) {
        return service.createReportForTask(taskId, solverId, report);
    }


    /**
     * Get reports by solver and task
     * PathVariable is used to extract values from the URL path and convert them into Long parameters
     * @return
     */
    @GetMapping("/solver/{solverId}")
    public List<Report> getReportsBySolver(@PathVariable Long solverId) {
        return service.findBySolver(solverId);
    }

    @GetMapping("/task/{taskId}")
    public List<Report> getReportsByTask(@PathVariable Long taskId) {
        return service.findByTask(taskId);
    }

    @DeleteMapping("/{reportId}")
    public void deleteReport(@PathVariable Long reportId) {
        service.deleteReport(reportId);
    }
}