package com.pandyzer.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Severidade")
public class Severity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "severidadeId")
    private Long id;
    @Column(name = "descricao")
    private String description;
    @Column(name = "peso")
    private Integer weight;

    public Severity () {}

    public Severity (Long id, String description, Integer weight) {

        this.id = id;
        this.description = description;
        this.weight = weight;

    }

    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public Integer getWeight() {
        return weight;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

}
