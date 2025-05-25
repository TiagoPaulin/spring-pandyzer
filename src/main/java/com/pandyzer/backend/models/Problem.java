package com.pandyzer.backend.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Problema")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "problemaId")
    private Long id;
    @Column(name = "usuarioId")
    private Long userId;
    @Column(name = "severidadeId")
    private Long severityId;
    @Column(name = "descricao")
    private String description;
    @Column(name = "recomendacao")
    private String recomendation;
    @Column(name = "data_cadastro")
    private Date register;
    @ManyToOne
    @JoinColumn(name = "heuristicaId", nullable = false)
    private Heuristic heuristic;

    public Problem () {}

    public Problem (Long id, Heuristic heuristic, Long userId, Long severityId, String description, String recomendation, Date register) {

        this.id = id;
        this.heuristic = heuristic;
        this.userId = userId;
        this.severityId = severityId;
        this.description = description;
        this.recomendation = recomendation;
        this.register = register;

    }

    public Long getId() {
        return id;
    }
    public Heuristic getHeuristic() {
        return heuristic;
    }
    public Long getUserId() {
        return userId;
    }
    public Long getSeverityId() {
        return severityId;
    }
    public String getDescription() {
        return description;
    }
    public String getRecomendation() {
        return recomendation;
    }
    public Date getRegister() {
        return register;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }
    public void setSeverityId(Long severityId) {
        this.severityId = severityId;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setRecomendation(String recomendation) {
        this.recomendation = recomendation;
    }
    public void setRegister(Date register) {
        this.register = register;
    }

}
