package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.User;
import com.pandyzer.backend.models.dto.LoginDTO;
import com.pandyzer.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDTO dto) {
        User user = service.authenticate(dto);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById (@PathVariable Long id) {

        User obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @GetMapping(value = "/avaliadores")
    public ResponseEntity<Optional<List<User>>> findAvaliadores() {
        Optional<List<User>> obj = service.findAvaliadores();
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/email/{email}")
    public ResponseEntity<User> findById (@PathVariable String email) {

        User obj = service.findByEmail(email);
        return ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<User> insert (@RequestBody User obj) {

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
    public ResponseEntity<User> update (@PathVariable Long id, @RequestBody User obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }

}
