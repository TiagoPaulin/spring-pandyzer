package com.pandyzer.backend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Evaluator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "avaliadorId")
    private Long id;
    @Column(name = "data_cadastro")
    private Date register;
    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "avaliacaoId", nullable = false)
    private Evaluation evaluation;
    @ManyToOne
    @JoinColumn(name = "statusId", nullable = false)
    private Status status;

    public Evaluator () {}

    public Evaluator (Long id, User user, Evaluation evaluation, Status status, Date register) {

        this.id = id;
        this.user = user;
        this.evaluation = evaluation;
        this.status = status;
        this.register = register;

    }

    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public Evaluation getEvaluation() {
        return evaluation;
    }
    public Date getRegister() {
        return register;
    }
    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
    public void setRegister(Date register) {
        this.register = register;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

}
