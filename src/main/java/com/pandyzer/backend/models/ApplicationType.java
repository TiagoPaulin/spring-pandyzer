package com.pandyzer.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ApplicationType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tipoAplicacaoId")
    private Long id;
    @Column(name = "descricao")
    private String description;
    @OneToMany(mappedBy = "applicationType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Evaluation> evaluations = new ArrayList<>();

    public ApplicationType () {}

    public ApplicationType (Long id, String description) {

        this.id = id;
        this.description = description;

    }

    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

}
