package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Problem;
import com.pandyzer.backend.models.UserType;
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
        List<Problem> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/objectives/{id}")
    public ResponseEntity<List<Problem>> findByObjectiveId (@PathVariable Long id) {

        List<Problem> list = service.findByObjectiveId(id);
        return ResponseEntity.ok().body(list);

    }

    @GetMapping(value = "/objectives/{id}/users/{id}")
    public ResponseEntity<List<Problem>> findByObjectiveIdAndUserId (@PathVariable Long objectiveId, @PathVariable Long userId) {

        List<Problem> list = service.findByObjectiveIdAndUserId(objectiveId, userId);
        return ResponseEntity.ok().body(list);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Problem> findById (@PathVariable Long id) {

        Problem obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Problem> insert (@RequestBody Problem obj) {

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
    public ResponseEntity<Problem> update (@PathVariable Long id, @RequestBody Problem obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }


}
