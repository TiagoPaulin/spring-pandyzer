package com.pandyzer.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usuarioId")
    private Long id;
    @Column(name = "nome")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "senha")
    private String password;
    @Column(name = "ativo")
    private Integer active;
    @Column(name = "data_cadastro")
    private Date register;
    @ManyToOne
    @JoinColumn(name = "tipUsuarioId", nullable = false)
    private UserType userType;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Evaluation> evaluations = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Problem> problems = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Evaluator> evaluators = new ArrayList<>();

    public User () {}

    public User (Long id, String name, String email, String password, Integer active, UserType userType, Date register) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = active;
        this.userType = userType;
        this.register = register;

    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public Integer getActive() {
        return active;
    }
    public UserType getUserType() {
        return userType;
    }
    public Date getRegister() {
        return register;
    }
    public List<Evaluation> getEvaluations() {
        return evaluations;
    }
    public List<Problem> getProblems() {
        return problems;
    }
    public List<Evaluator> getEvaluators() {
        return evaluators;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setActive(Integer active) {
        this.active = active;
    }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    public void setRegister(Date register) {
        this.register = register;
    }
    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
    public void setEvaluators(List<Evaluator> evaluators) {
        this.evaluators = evaluators;
    }

}
