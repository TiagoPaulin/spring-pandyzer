package com.pandyzer.backend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Evaluator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "avaliadorId")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "avaliacaoId", nullable = false)
    private Evaluation evaluation;
    @Column(name = "data_cadastro")
    private Date register;

    public Evaluator () {}

    public Evaluator (Long id, User user, Evaluation evaluation, Date register) {

        this.id = id;
        this.user = user;
        this.evaluation = evaluation;
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

}
