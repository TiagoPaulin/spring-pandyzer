package com.pandyzer.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "statusId")
    private Long id;
    @Column(name = "descricao")
    private String description;
    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Evaluator> evaluators = new ArrayList<>();


    public Status () {}

    public Status (Long id, String description) {

        this.id = id;
        this.description = description;

    }

    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public List<Evaluator> getEvaluators() {
        return evaluators;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setEvaluators(List<Evaluator> evaluators) {
        this.evaluators = evaluators;
    }

}
