package com.example.teachingmanagement.controller;

import com.example.teachingmanagement.model.Criterion;
import com.example.teachingmanagement.service.CriterionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criteria")
public class CriterionController {

    private final CriterionService service;

    public CriterionController(CriterionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Criterion> create(@RequestBody Criterion criterion) {
        Criterion saved = service.save(criterion);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Criterion>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Criterion> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Criterion>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.findByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Criterion> update(@PathVariable Long id, @RequestBody Criterion criterion) {
        try {
            Criterion updated = service.update(id, criterion);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
