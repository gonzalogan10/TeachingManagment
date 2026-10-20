package com.example.teachingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.teachingmanagement.model.Solver;

public interface SolverRepository extends JpaRepository<Solver, Long> {
}