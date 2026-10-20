package com.example.teachingmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.teachingmanagement.model.Report;
import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository repository;
    private final SolverService solverService;
    private final TaskService taskService;

    public ReportService(ReportRepository repository, SolverService solverService, TaskService taskService) {
        this.repository = repository;
        this.solverService = solverService;
        this.taskService = taskService;
    }

    public Report save(Report report) {
        return repository.save(report);
    }

    public List<Report> findAll() {
        return repository.findAll();
    }

    public Report createReportForTask(Long taskId, Long solverId, Report report) {
        Task task = taskService.findById(taskId);
        Solver solver = solverService.findById(solverId);
        report.setTask(task);
        report.setSolver(solver);
        return repository.save(report);
    }

    public List<Report> findBySolver(Long solverId) {
        Solver solver = solverService.findById(solverId);
        return repository.findBySolver(solver);
    }

    public List<Report> findByTask(Long taskId) {
        Task task = taskService.findById(taskId);
        return repository.findByTask(task);
    }

    public void deleteReport(Long reportId) {
        repository.deleteById(reportId);
    }
}