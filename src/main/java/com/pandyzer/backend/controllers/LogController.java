package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Log;
import com.pandyzer.backend.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/logs")
public class LogController {

    @Autowired
    private LogService service;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Log>> getLogsForUserCreatedEvaluations(@PathVariable Long userId) {
        List<Log> logs = service.findLogsForUserCreatedEvaluations(userId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping
    public ResponseEntity<List<Log>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Log> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Log> insert(@RequestBody Log log) {
        Log newLog = service.insert(log);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newLog.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Log> update(@PathVariable Long id, @RequestBody Log logDetails) {
        return service.findById(id)
                .map(log -> {
                    log.setDescription(logDetails.getDescription());
                    Log updatedLog = service.insert(log);
                    return ResponseEntity.ok(updatedLog);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}