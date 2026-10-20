package com.example.teachingmanagement;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.teachingmanagement.model.Criterion;
import com.example.teachingmanagement.model.EvaluationMethod;
import com.example.teachingmanagement.service.CriterionService;

@SpringBootTest
class CriterionServiceTest {

    @Autowired
    private CriterionService criterionService;

    @Test
    void saveCriterion() {
        Criterion criterion = new Criterion(
            "Documentation",
            "README Quality",
            "Check if README is complete",
            EvaluationMethod.PER_TEAM,
            10.0,
            true,
            "README needs improvement"
        );

        Criterion saved = criterionService.save(criterion);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCategory()).isEqualTo("Documentation");
        assertThat(saved.getName()).isEqualTo("README Quality");
    }

    @Test
    void findAllCriteria() {
        criterionService.save(new Criterion(
            "Testing",
            "Unit Tests",
            "Check unit test coverage",
            EvaluationMethod.PER_TEAM,
            20.0,
            false,
            "Add more tests"
        ));

        List<Criterion> criteria = criterionService.findAll();

        assertThat(criteria).isNotNull();
        assertThat(criteria).isNotEmpty();
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
        Criterion saved = criterionService.save(criterion);

        Optional<Criterion> found = criterionService.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Clean Code");
    }

    @Test
    void findByCategory() {
        criterionService.save(new Criterion(
            "Documentation",
            "README Quality",
            "Check README",
            EvaluationMethod.PER_TEAM,
            10.0,
            true,
            "Needs improvement"
        ));
        criterionService.save(new Criterion(
            "Documentation",
            "API Docs",
            "Check API documentation",
            EvaluationMethod.PER_TEAM,
            5.0,
            false,
            "Add API docs"
        ));

        List<Criterion> docCriteria = criterionService.findByCategory("Documentation");

        assertThat(docCriteria).hasSizeGreaterThanOrEqualTo(2);
        assertThat(docCriteria).allMatch(c -> c.getCategory().equalsIgnoreCase("Documentation"));
    }

    @Test
    void findByCategoryCaseInsensitive() {
        criterionService.save(new Criterion(
            "Security",
            "Authentication",
            "Check authentication implementation",
            EvaluationMethod.PER_TEAM,
            15.0,
            false,
            "Improve security"
        ));

        List<Criterion> found = criterionService.findByCategory("security");

        assertThat(found).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void updateCriterion() {
        Criterion criterion = new Criterion(
            "Performance",
            "Load Time",
            "Check application load time",
            EvaluationMethod.PER_TEAM,
            10.0,
            false,
            "Optimize load time"
        );
        Criterion saved = criterionService.save(criterion);

        Criterion update = new Criterion(
            "Performance",
            "Load Time Optimized",
            "Check optimized application load time",
            EvaluationMethod.PER_TEAM,
            15.0,
            true,
            "Further optimization needed"
        );

        Criterion updated = criterionService.update(saved.getId(), update);

        assertThat(updated.getName()).isEqualTo("Load Time Optimized");
        assertThat(updated.getMaxPoints()).isEqualTo(15.0);
        assertThat(updated.isPointsRedistribution()).isTrue();
        assertThat(updated.getDefaultFeedback()).isEqualTo("Further optimization needed");
    }

    @Test
    void updateNonExistentCriterion() {
        Criterion update = new Criterion(
            "NonExistent",
            "Test",
            "Test",
            EvaluationMethod.PER_TEAM,
            10.0,
            false,
            "Test"
        );

        assertThrows(RuntimeException.class, () -> {
            criterionService.update(99999L, update);
        });
    }

    @Test
    void deleteCriterion() {
        Criterion criterion = new Criterion(
            "ToDelete",
            "Delete Test",
            "This will be deleted",
            EvaluationMethod.PER_TEAM,
            5.0,
            false,
            "Test feedback"
        );
        Criterion saved = criterionService.save(criterion);

        criterionService.deleteById(saved.getId());

        Optional<Criterion> found = criterionService.findById(saved.getId());
        assertThat(found).isNotPresent();
    }

    @Test
    void saveCriterionWithPerStudentEvaluation() {
        Criterion criterion = new Criterion(
            "Individual",
            "Personal Work",
            "Evaluate individual contribution",
            EvaluationMethod.PER_STUDENT,
            30.0,
            true,
            "Improve individual work"
        );

        Criterion saved = criterionService.save(criterion);

        assertThat(saved.getEvaluationMethod()).isEqualTo(EvaluationMethod.PER_STUDENT);
        assertThat(saved.getMaxPoints()).isEqualTo(30.0);
    }

    @Test
    void saveCriterionWithLongDescription() {
        String longProcedure = "This is a very detailed checking procedure that describes " +
                              "exactly how to evaluate this criterion step by step. " +
                              "It includes multiple paragraphs and detailed instructions " +
                              "for the evaluator to follow carefully.";
        
        Criterion criterion = new Criterion(
            "Detailed",
            "Complex Criterion",
            longProcedure,
            EvaluationMethod.PER_TEAM,
            25.0,
            false,
            "Detailed feedback for students"
        );

        Criterion saved = criterionService.save(criterion);

        assertThat(saved.getCheckingProcedure()).isEqualTo(longProcedure);
        assertThat(saved.getCheckingProcedure().length()).isGreaterThan(100);
    }
}
