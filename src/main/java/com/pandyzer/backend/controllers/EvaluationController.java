package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Evaluation;
import com.pandyzer.backend.models.dto.QuantityDTO;
import com.pandyzer.backend.services.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService service;

    @GetMapping
    public ResponseEntity<List<Evaluation>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/community/{userId}")
    public ResponseEntity<List<Evaluation>> findCommunityEvaluations(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findCommunityEvaluations(userId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Evaluation>> filterEvaluations(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) Long creatorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date finalDate
    ) {
        return ResponseEntity.ok(
                service.filterEvaluations(description, startDate, finalDate, statusId, creatorId)
        );
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<QuantityDTO> countEvaluations(@PathVariable Long id) {
        long count = service.countEvaluations(id);
        // QuantityDTO espera Integer — converte com segurança:
        int safe = Math.toIntExact(count);
        return ResponseEntity.ok(new QuantityDTO(safe));
    }

    @GetMapping("/creator/{userId}")
    public ResponseEntity<List<Evaluation>> getByCreator(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getByCreator(userId));
    }

    @PostMapping
    public ResponseEntity<Evaluation> insert(@RequestBody Evaluation obj) {
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> update(@PathVariable Long id, @RequestBody Evaluation obj) {
        return ResponseEntity.ok(service.update(id, obj));
    }

    // EvaluationController.java
    @GetMapping("/by-evaluator/{userId}")
    public ResponseEntity<List<Evaluation>> getByEvaluator(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByEvaluatorUser(userId));
    }

}
