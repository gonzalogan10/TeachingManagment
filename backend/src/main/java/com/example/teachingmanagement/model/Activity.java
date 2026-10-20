package com.example.teachingmanagement.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @ManyToOne
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    private List<Solver> solvers = new ArrayList<>();

    // 🔹 CONSTRUCTEUR VIDE (OBLIGATOIRE)
    public Activity() {
    }

    public Activity(String name, ActivityType type, Course course) {
        this.name = name;
        this.type = type;
        this.course = course;
    }

    // 🔹 GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Solver> getSolvers() {
        return solvers;
    }

    public void setSolvers(List<Solver> solvers) {
        this.solvers = solvers;
    }
}
