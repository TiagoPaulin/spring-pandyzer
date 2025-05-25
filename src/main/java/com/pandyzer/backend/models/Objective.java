package com.pandyzer.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Objetivo")
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "objetivoId")
    private Long id;
    @Column(name = "descricao")
    private String description;
    @Column(name = "data_cadastro")
    private Date register;
    @ManyToOne
    @JoinColumn(name = "avaliacaoId", nullable = false)
    private Evaluation evaluation;
    @ManyToOne
    @JoinColumn(name = "statusId", nullable = false)
    private Status status;
    @OneToMany(mappedBy = "objective", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Problem> problems = new ArrayList<>();

    public Objective () {}

    public Objective (Long id, String description, Date register, Evaluation evaluation, Status status) {

        this.id = id;
        this.description = description;
        this.register = register;
        this.evaluation = evaluation;
        this.status = status;

    }

    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public Date getRegister() {
        return register;
    }
    public List<Problem> getProblems() {
        return problems;
    }
    public Evaluation getEvaluation() {
        return evaluation;
    }
    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setRegister(Date register) {
        this.register = register;
    }
    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

}
