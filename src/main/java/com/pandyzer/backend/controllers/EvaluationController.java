package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.Evaluation;
import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.models.dto.QuantityDTO;
import com.pandyzer.backend.services.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService service;

    @GetMapping
    public ResponseEntity<List<Evaluation>> findAll() {
        List<Evaluation> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Evaluation> findById (@PathVariable Long id) {

        Evaluation obj = service.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @GetMapping("/community/{userId}")
    public ResponseEntity<List<Evaluation>> findCommunityEvaluations(@PathVariable Long userId) {
        List<Evaluation> communityList = service.findCommunityEvaluations(userId);
        return ResponseEntity.ok(communityList);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Evaluation>> filterEvaluations(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date finalDate,
            @RequestParam(required = false) Long statusId) {

        List<Evaluation> list = service.filterEvaluations(description, startDate, finalDate, statusId);
        return ResponseEntity.ok().body(list);

    }

    @GetMapping("/count/{id}")
    public ResponseEntity<QuantityDTO> countEvaluations (@PathVariable Long id) {

        QuantityDTO obj = new QuantityDTO(service.countEvaluations(id));
        return  ResponseEntity.ok().body(obj);

    }

    @PostMapping
    public ResponseEntity<Evaluation> insert (@RequestBody Evaluation obj) {

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
    public ResponseEntity<Evaluation> update (@PathVariable Long id, @RequestBody Evaluation obj) {

        obj = service.update(id, obj);
        return ResponseEntity.ok().body(obj);

    }

}