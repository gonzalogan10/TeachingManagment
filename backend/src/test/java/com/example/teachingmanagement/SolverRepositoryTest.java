package com.example.teachingmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.teachingmanagement.model.Solver;
import com.example.teachingmanagement.repository.SolverRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class SolverRepositoryTest {

    @Autowired
    private SolverRepository solverRepository;

    private Solver solver;

    @BeforeEach
    void setUp() {
        solver = new Solver("Sara");
    }

    @Test
    void createSolver() {
        Solver saved = solverRepository.save(solver);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Sara");
    }

    @Test
    void findSolverById() {
        Solver saved = solverRepository.save(solver);

        Optional<Solver> found = solverRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Sara");
    }

    @Test
    void findAllSolvers() {
        solverRepository.save(solver);
        solverRepository.save(new Solver("John"));

        List<Solver> solvers = solverRepository.findAll();
        assertThat(solvers).hasSize(2);
    }

    @Test
    void updateSolver() {
        Solver saved = solverRepository.save(solver);

        saved.setName("Sara Updated");
        Solver updated = solverRepository.save(saved);

        assertThat(updated.getName()).isEqualTo("Sara Updated");
    }

    @Test
    void deleteSolver() {
        Solver saved = solverRepository.save(solver);

        solverRepository.deleteById(saved.getId());

        Optional<Solver> found = solverRepository.findById(saved.getId());
        assertThat(found).isNotPresent();
    }
}
