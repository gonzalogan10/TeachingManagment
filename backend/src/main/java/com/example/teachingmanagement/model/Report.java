package com.example.teachingmanagement.model;

import jakarta.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String content;

    private String timeSpent; // e.g., "2 hours"
    private double proportion;
    private int numStudents;

    @ManyToOne
    private Solver solver; 
    @ManyToOne
    private Task task; 


    public Report() {
    }

    public Report(String content) {
        this.content = content;
    }

    public Report(String content, String time, double proportion, int students, Solver solver, Task task) {
        this.content = content;
        this.timeSpent = time;
        this.proportion = proportion;
        this.numStudents = students;
        this.solver = solver;
        this.task = task;
    }

    // 🔹 GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(int numberOfStudents) {
        this.numStudents = numberOfStudents;
    }

    public Solver getSolver() {
        return solver;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}