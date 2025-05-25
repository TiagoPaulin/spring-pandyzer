package com.pandyzer.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Avaliacao")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "avaliacaoId")
    private Long id;
    @Column(name = "descricao")
    private String description;
    @Column(name = "dataInicial")
    private Date startDate;
    @Column(name = "dataFinal")
    private Date finalDate;
    @Column(name = "link")
    private String link;
    @Column(name = "data_cadastro")
    private Date register;
    @ManyToOne
    @JoinColumn(name = "statusId", nullable = false)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "tipoAplicacaoId", nullable = false)
    private ApplicationType applicationType;
    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private User user;
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Objective> objectives = new ArrayList<>();

    public Evaluation () {}

    public Evaluation (Long id, String description, Date startDate, Date finalDate, String link, ApplicationType applicationType, Status status, Date register, User user) {

        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.link = link;
        this.applicationType = applicationType;
        this.status = status;
        this.register = register;
        this.user = user;

    }

    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getFinalDate() {
        return finalDate;
    }
    public String getLink() {
        return link;
    }
    public ApplicationType getApplicationType() {
        return applicationType;
    }
    public Status getStatus() {
        return status;
    }
    public Date getRegister() {
        return  register;
    }
    public List<Objective> getObjectives() {
        return objectives;
    }
    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setRegister(Date register) {
        this.register = register;
    }
    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }
    public void setUser(User user) {
        this.user = user;
    }

}