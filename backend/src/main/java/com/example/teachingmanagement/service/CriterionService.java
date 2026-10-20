package com.example.teachingmanagement.service;

import com.example.teachingmanagement.model.Criterion;
import com.example.teachingmanagement.repository.CriterionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CriterionService {

    private final CriterionRepository repository;

    public CriterionService(CriterionRepository repository) {
        this.repository = repository;
    }

    public Criterion save(Criterion criterion) {
        return repository.save(criterion);
    }

    public List<Criterion> findAll() {
        return repository.findAll();
    }

    public Optional<Criterion> findById(Long id) {
        return repository.findById(id);
    }

    public List<Criterion> findByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category);
    }

    public Criterion update(Long id, Criterion updatedCriterion) {
        return repository.findById(id)
                .map(criterion -> {
                    criterion.setCategory(updatedCriterion.getCategory());
                    criterion.setName(updatedCriterion.getName());
                    criterion.setCheckingProcedure(updatedCriterion.getCheckingProcedure());
                    criterion.setEvaluationMethod(updatedCriterion.getEvaluationMethod());
                    criterion.setMaxPoints(updatedCriterion.getMaxPoints());
                    criterion.setPointsRedistribution(updatedCriterion.isPointsRedistribution());
                    criterion.setDefaultFeedback(updatedCriterion.getDefaultFeedback());
                    return repository.save(criterion);
                })
                .orElseThrow(() -> new RuntimeException("Criterion not found with id: " + id));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
