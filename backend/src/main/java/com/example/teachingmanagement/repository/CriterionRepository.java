package com.example.teachingmanagement.repository;

import com.example.teachingmanagement.model.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CriterionRepository extends JpaRepository<Criterion, Long> {
    
    // Find criteria by category
    List<Criterion> findByCategory(String category);
    
    // Find criteria by category (case-insensitive)
    List<Criterion> findByCategoryIgnoreCase(String category);
}
