package com.pandyzer.backend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Objetivo")
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "objetivoId")
    private Long id;
    @Column(name = "descricao")
    private String description;
    @Column(name = "avaliacaoId")
    private Integer evaluationId;
    @Column(name = "data_cadastro")
    private Date register;

    public Objective () {}

    public Objective (Long id, String description, Integer evaluationId, Date register) {

        this.id = id;
        this.description = description;
        this.evaluationId = evaluationId;
        this.register = register;

    }

    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public Integer getEvaluationId() {
        return evaluationId;
    }
    public Date getRegister() {
        return register;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setEvaluationId(Integer evaluationId) {
        this.evaluationId = evaluationId;
    }
    public void setRegister(Date register) {
        this.register = register;
    }

}
