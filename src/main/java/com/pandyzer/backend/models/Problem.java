package com.pandyzer.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "descricao")
    private String description;
    @Column(name = "recomendacao")
    private String recomendation;
    @Column(name = "data_cadastro")
    private Date register;
    @ManyToOne
    @JoinColumn(name = "objetivoId", nullable = false)
    private Objective objective;
    @ManyToOne
    @JoinColumn(name = "heuristicaId", nullable = false)
    private Heuristic heuristic;
    @ManyToOne
    @JoinColumn(name = "severidadeId", nullable = false)
    private Severity severity;
    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private User user;

    public Problem () {}

    public Problem (Long id, User user, String description, String recomendation, Date register, Objective objective, Heuristic heuristic, Severity severity) {

        this.id = id;
        this.heuristic = heuristic;
        this.user = user;
        this.severity = severity;
        this.description = description;
        this.recomendation = recomendation;
        this.register = register;
        this.objective = objective;

    }

    public Long getId() {
        return id;
    }
    public Heuristic getHeuristic() {
        return heuristic;
    }
    public User getUser() {
        return user;
    }
    public Severity getSeverity() {
        return severity;
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
    public Objective getObjective() {
        return objective;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }
    public void setSeverity(Severity severity) {
        this.severity = severity;
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
    public void setObjective(Objective objective) {
        this.objective = objective;
    }

}
