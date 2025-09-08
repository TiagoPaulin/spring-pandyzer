package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Problem;
import com.pandyzer.backend.services.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/problems")
public class ProblemController {

    @Autowired
    private ProblemService service;

    @GetMapping
    public ResponseEntity<List<Problem>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "/objectives/{id}")
    public ResponseEntity<List<Problem>> findByObjectiveId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByObjectiveId(id));
    }

    @GetMapping(value = "/objectives/{objectiveId}/users/{userId}")
    public ResponseEntity<List<Problem>> findByObjectiveIdAndUserId(
            @PathVariable Long objectiveId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(service.findByObjectiveIdAndUserId(objectiveId, userId));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Problem> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Problem> insert(@RequestBody Problem obj) {
        Problem saved = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Problem> update(@PathVariable Long id, @RequestBody Problem obj) {
        return ResponseEntity.ok(service.update(id, obj));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
