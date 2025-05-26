package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Evaluation;
import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.services.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService service;

    @GetMapping
    public ResponseEntity<List<Evaluation>> findAll() {
        List<Evaluation> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Evaluation> findById (@PathVariable Long id) {

        Evaluation obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Evaluation> insert (@RequestBody Evaluation obj) {

        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Evaluation> update (@PathVariable Long id, @RequestBody Evaluation obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }

}