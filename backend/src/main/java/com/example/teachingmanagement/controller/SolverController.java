package com.example.teachingmanagement.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.service.SolverService;

@RestController
@RequestMapping("/api/solvers")
public class SolverController {

    private final SolverService solverService;

    public SolverController(SolverService solverService) {
        this.solverService = solverService;
    }

    @PostMapping
    public Solver createSolver(@RequestBody Solver solver) {
        return solverService.saveSolver(solver);
    }

    @GetMapping
    public List<Solver> getAllSolvers() {
        return solverService.findAllSolvers();
    }
    
    /**
     * Endpoint to add a solver to a task: To add a specific solver (solverId) to a specific task (taskId)
     * PathVariable is used to extract values from the URL path and convert them into Long parameters.
     * @param solverId ID of the solver to be added
     * @param taskId ID of the task to which the solver will be added
     * 
     **/
    @PostMapping("/{solverId}/tasks/{taskId}")
    public void addSolverToTask(@PathVariable Long solverId, @PathVariable Long taskId) {
        solverService.addSolverToTask(taskId, solverId);
    }

    @PostMapping("/{solverId}/main-solver/{taskId}")
    public void setMainSolver(@PathVariable Long solverId, @PathVariable Long taskId) {
        solverService.setMainSolver(solverId, taskId);
    }

    @DeleteMapping("/{solverId}/tasks/{taskId}")
    public void removeSolverFromTask(@PathVariable Long solverId, @PathVariable Long taskId) {
        solverService.removeSolverFromTask(taskId, solverId);
    }
}
