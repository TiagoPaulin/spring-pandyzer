package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.ApplicationType;
import com.pandyzer.backend.services.ApplicationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/applicationtype")
public class ApplicationTypeController {

    @Autowired
    private ApplicationTypeService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ApplicationType> findById (@PathVariable Long id) {

        ApplicationType obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<ApplicationType> insert (@RequestBody ApplicationType obj) {

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
    public ResponseEntity<ApplicationType> update (@PathVariable Long id, @RequestBody ApplicationType obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }

}
