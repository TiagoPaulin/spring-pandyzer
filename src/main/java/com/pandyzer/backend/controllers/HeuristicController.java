package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Heuristic;
import com.pandyzer.backend.services.HeuristicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/heuristics")
public class HeuristicController {

    @Autowired private HeuristicService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Heuristic> findById (@PathVariable Long id) {

        Heuristic obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Heuristic> insert (@RequestBody Heuristic obj) {

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
    public ResponseEntity<Heuristic> update (@PathVariable Long id, @RequestBody Heuristic obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }

}
