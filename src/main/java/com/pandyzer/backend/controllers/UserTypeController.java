package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.services.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/usertype")
public class UserTypeController {

    @Autowired
    private UserTypeService service;

    @GetMapping
    public ResponseEntity<List<UserType>> findAll() {
        List<UserType> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserType> findById (@PathVariable Long id) {

        UserType obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<UserType> insert (@RequestBody UserType obj) {

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
    public ResponseEntity<UserType> update (@PathVariable Long id, @RequestBody UserType obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }

}
