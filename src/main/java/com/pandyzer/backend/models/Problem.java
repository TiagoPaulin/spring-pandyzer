package com.pandyzer.backend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Problema")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "problemaId")
    private Long id;
    @Column(name = "heuristicaId")
    private Long heuristicId;
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

    public Problem () {}

    public Problem (Long id, Long heuristicId, Long userId, Long severityId, String description, String recomendation, Date register) {

        this.id = id;
        this.heuristicId = heuristicId;
        this.userId = userId;
        this.severityId = severityId;
        this.description = description;
        this.recomendation = recomendation;
        this.register = register;

    }

    public Long getId() {
        return id;
    }
    public Long getHeuristicId() {
        return heuristicId;
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
    public void setHeuristicId(Long heuristicId) {
        this.heuristicId = heuristicId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
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
