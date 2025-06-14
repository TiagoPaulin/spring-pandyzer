package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Evaluator;
import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.services.EvaluatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/evaluators")
public class EvaluatorController {

    @Autowired
    private EvaluatorService service;

    @GetMapping
    public ResponseEntity<List<Evaluator>> findAll() {
        List<Evaluator> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Evaluator> findById (@PathVariable Long id) {

        Evaluator obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @GetMapping(value = "/evaluation/{id}")
    public ResponseEntity<List<Evaluator>> findByIdEvaluation (@PathVariable Long id) {

        List<Evaluator> obj = service.findByEvaluationId(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Evaluator> insert (@RequestBody Evaluator obj) {

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
    public ResponseEntity<Evaluator> update (@PathVariable Long id, @RequestBody Evaluator obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }

    @PutMapping(value = "/{idUser}/{idEvaluation}/status/{idStatus}")
    public ResponseEntity<Evaluator> updateStatusEvaluator (@PathVariable Long idUser, @PathVariable Long idEvaluation, @PathVariable Long idStatus) {

        Evaluator evaluator = service.updateStatusEvaluator(idUser, idEvaluation, idStatus);
        return ResponseEntity.ok().body(evaluator);

    }
}
