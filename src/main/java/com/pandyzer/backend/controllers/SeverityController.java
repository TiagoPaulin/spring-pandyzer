package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Severity;
import com.pandyzer.backend.models.User;
import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.services.SeverityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/severities")
public class SeverityController {

    @Autowired
    private SeverityService service;

    @GetMapping
    public ResponseEntity<List<Severity>> findAll() {
        List<Severity> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Severity> findById (@PathVariable Long id) {

        Severity obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Severity> insert (@RequestBody Severity obj) {

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
    public ResponseEntity<Severity> update (@PathVariable Long id, @RequestBody Severity obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }


}
