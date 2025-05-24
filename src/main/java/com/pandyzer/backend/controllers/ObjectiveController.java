package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Objective;
import com.pandyzer.backend.models.User;
import com.pandyzer.backend.services.ObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/objectives")
public class ObjectiveController {

    @Autowired
    private ObjectiveService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Objective> findById (@PathVariable Long id) {

        Objective obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Objective> insert (@RequestBody Objective obj) {

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
    public ResponseEntity<Objective> update (@PathVariable Long id, @RequestBody Objective obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }


}
