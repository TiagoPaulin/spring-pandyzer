package com.pandyzer.backend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "logId")
    private Long id;

    @Column(name = "descricao")
    private String description;

    @ManyToOne
    @JoinColumn(name = "usuarioId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "avaliacaoId")
    private Evaluation evaluation;

    @CreationTimestamp
    @Column(name = "dataHora")
    private LocalDateTime logTimestamp;

    public Log() {}

    public Log(Long id, String description, User user, Evaluation evaluation, LocalDateTime logTimestamp) {
        this.id = id;
        this.description = description;
        this.user = user;
        this.evaluation = evaluation;
        this.logTimestamp = logTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public LocalDateTime getLogTimestamp() {
        return logTimestamp;
    }

    public void setLogTimestamp(LocalDateTime logTimestamp) {
        this.logTimestamp = logTimestamp;
    }
}