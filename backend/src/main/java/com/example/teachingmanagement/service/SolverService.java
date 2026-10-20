package com.example.teachingmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.model.Task;
import com.example.teachingmanagement.repository.SolverRepository;
import com.example.teachingmanagement.repository.TaskRepository;

@Service
public class SolverService {

    private final SolverRepository solverRepository;
    private final TaskRepository taskRepository;

    public SolverService(SolverRepository solverRepository, TaskRepository taskRepository) {
        this.solverRepository = solverRepository;
        this.taskRepository = taskRepository;
    }

    public Solver saveSolver(Solver solver) {
        return solverRepository.save(solver);
    }

    public List<Solver> findAllSolvers() {
        return solverRepository.findAll();
    }

    public void addSolverToTask(Long taskId, Long solverId) {

        //First, search for the task and solver by their IDs
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Solver solver = findById(solverId);

        task.addSolver(solver);
        taskRepository.save(task);
    }

    public void removeSolverFromTask(Long taskId, Long solverId) {

        //First, search for the task and solver by their IDs
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Solver solver = findById(solverId);

        // Remove the solver from the task's solvers list
        task.getSolvers().remove(solver);
        
        // If the removed solver was the main solver, clear the main solver
        if (task.getMainSolver() != null && task.getMainSolver().getId().equals(solverId)) {
            task.setMainSolver(null);
        }
        
        taskRepository.save(task);
    }

     public void setMainSolver(Long solverId, Long taskId) {

        //First, search for the task and solver by their IDs
        Solver solver = findById(solverId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

    
        task.addSolver(solver);
        task.setMainSolver(solver);
        taskRepository.save(task);
    }

    public Solver findById(Long id) {
        return solverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solver not found"));
    }
}