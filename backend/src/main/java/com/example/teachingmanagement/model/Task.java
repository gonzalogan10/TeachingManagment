package com.example.teachingmanagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int taskOrder;
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JsonIgnore
    private Activity activity;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Solver> solvers = new ArrayList<>();

    @ManyToOne
    private Solver mainSolver;

    // 🔹 CONSTRUCTEUR VIDE (OBLIGATOIRE)
    public Task() {
    }

    public Task(String title, Activity activity) {
        this.title = title;
        this.activity = activity;
    }

    public Task(int taskOrder, String title, String description, LocalDate startDate, LocalDate endDate, Activity activity) {
        this.taskOrder = taskOrder;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.activity = activity;
    }

    // 🔹 GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(int taskOrder) {
        this.taskOrder = taskOrder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Solver> getSolvers() {
        return solvers;
    }

    public void setSolvers(List<Solver> solvers) {
        this.solvers = solvers;
    }

    public Solver getMainSolver() {
        return mainSolver;
    }

    public void setMainSolver(Solver mainSolver) {
        this.mainSolver = mainSolver;
    }

    /**
     * Add a solver to the task if not already present
     * @param solver
     * @return true if the solver was added, false otherwise
     */
    public boolean addSolver(Solver solver) {
        if(this.solvers.contains(solver) || solver == null) {
        return false;
    }
    this.solvers.add(solver);
    return true;
}


}
