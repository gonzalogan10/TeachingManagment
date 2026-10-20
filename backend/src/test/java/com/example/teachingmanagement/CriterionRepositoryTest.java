package com.example.teachingmanagement;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.teachingmanagement.model.Criterion;
import com.example.teachingmanagement.model.EvaluationMethod;
import com.example.teachingmanagement.repository.CriterionRepository;

@DataJpaTest
class CriterionRepositoryTest {

    @Autowired
    private CriterionRepository criterionRepository;

    @Test
    void createCriterion() {
        Criterion criterion = new Criterion(
            "Documentation",
            "README Quality",
            "Check if README is complete and well-structured",
            EvaluationMethod.PER_TEAM,
            10.0,
            true,
            "README needs improvement"
        );

        Criterion saved = criterionRepository.save(criterion);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCategory()).isEqualTo("Documentation");
        assertThat(saved.getName()).isEqualTo("README Quality");
        assertThat(saved.getEvaluationMethod()).isEqualTo(EvaluationMethod.PER_TEAM);
        assertThat(saved.getMaxPoints()).isEqualTo(10.0);
        assertThat(saved.isPointsRedistribution()).isTrue();
    }

    @Test
    void findCriterionById() {
        Criterion criterion = new Criterion(
            "Code Quality",
            "Clean Code",
            "Check code follows clean code principles",
            EvaluationMethod.PER_STUDENT,
            15.0,
            false,
            "Code needs refactoring"
        );
        Criterion saved = criterionRepository.save(criterion);

        Optional<Criterion> found = criterionRepository.findById(saved.getId());
        
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Clean Code");
        assertThat(found.get().getCategory()).isEqualTo("Code Quality");
    }

    @Test
    void findAllCriteria() {
        criterionRepository.save(new Criterion(
            "Documentation",
            "README Quality",
            "Check README",
            EvaluationMethod.PER_TEAM,
            10.0,
            true,
            "Needs improvement"
        ));
        criterionRepository.save(new Criterion(
            "Testing",
            "Unit Tests",
            "Check unit test coverage",
            EvaluationMethod.PER_TEAM,
            20.0,
            false,
            "Add more tests"
        ));

        List<Criterion> criteria = criterionRepository.findAll();
        
        assertThat(criteria).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void findByCategory() {
        criterionRepository.save(new Criterion(
            "Documentation",
            "README Quality",
            "Check README",
            EvaluationMethod.PER_TEAM,
            10.0,
            true,
            "Needs improvement"
        ));
        criterionRepository.save(new Criterion(
            "Documentation",
            "API Docs",
            "Check API documentation",
            EvaluationMethod.PER_TEAM,
            5.0,
            false,
            "Add API docs"
        ));
        criterionRepository.save(new Criterion(
            "Testing",
            "Unit Tests",
            "Check unit tests",
            EvaluationMethod.PER_TEAM,
            20.0,
            false,
            "Add tests"
        ));

        List<Criterion> docCriteria = criterionRepository.findByCategory("Documentation");
        
        assertThat(docCriteria).hasSize(2);
        assertThat(docCriteria).allMatch(c -> c.getCategory().equals("Documentation"));
    }

    @Test
    void findByCategoryCaseInsensitive() {
        criterionRepository.save(new Criterion(
            "Testing",
            "Unit Tests",
            "Check unit tests",
            EvaluationMethod.PER_TEAM,
            20.0,
            false,
            "Add tests"
        ));

        List<Criterion> found = criterionRepository.findByCategoryIgnoreCase("testing");
        
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getCategory()).isEqualTo("Testing");
    }

    @Test
    void updateCriterion() {
        Criterion criterion = new Criterion(
            "Documentation",
            "README Quality",
            "Check README",
            EvaluationMethod.PER_TEAM,
            10.0,
            true,
            "Needs improvement"
        );
        Criterion saved = criterionRepository.save(criterion);

        saved.setMaxPoints(15.0);
        saved.setDefaultFeedback("Please improve documentation");
        Criterion updated = criterionRepository.save(saved);

        assertThat(updated.getMaxPoints()).isEqualTo(15.0);
        assertThat(updated.getDefaultFeedback()).isEqualTo("Please improve documentation");
    }

    @Test
    void deleteCriterion() {
        Criterion criterion = new Criterion(
            "Documentation",
            "README Quality",
            "Check README",
            EvaluationMethod.PER_TEAM,
            10.0,
            true,
            "Needs improvement"
        );
        Criterion saved = criterionRepository.save(criterion);

        criterionRepository.deleteById(saved.getId());

        Optional<Criterion> found = criterionRepository.findById(saved.getId());
        assertThat(found).isNotPresent();
    }

    @Test
    void saveCriterionWithPerStudentEvaluation() {
        Criterion criterion = new Criterion(
            "Individual Work",
            "Personal Contribution",
            "Evaluate individual student contribution",
            EvaluationMethod.PER_STUDENT,
            25.0,
            true,
            "Contribution is below expectations"
        );

        Criterion saved = criterionRepository.save(criterion);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEvaluationMethod()).isEqualTo(EvaluationMethod.PER_STUDENT);
    }
}
