package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Status;
import com.pandyzer.backend.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/status")
public class StatusController {

    @Autowired
    private StatusService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Status> findById (@PathVariable Long id) {

        Status obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Status> insert (@RequestBody Status obj) {

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
    public ResponseEntity<Status> update (@PathVariable Long id, @RequestBody Status obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }

}
