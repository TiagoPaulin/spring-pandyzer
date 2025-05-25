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
    @Column(name = "tipoAplicacaoId")
    private Integer applicationType;
    @Column(name = "usuarioId")
    private Integer userId;
    @Column(name = "statusId")
    private Integer statusId;
    @Column(name = "data_cadastro")
    private Date register;
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Objective> objectives = new ArrayList<>();

    public Evaluation () {}

    public Evaluation (Long id, String description, Date startDate, Date finalDate, String link, Integer applicationType, Integer userId, Integer statusId, Date register) {

        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.link = link;
        this.applicationType = applicationType;
        this.userId = userId;
        this.statusId = statusId;
        this.register = register;

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
    public Integer getApplicationType() {
        return applicationType;
    }
    public Integer getUserId() {
        return userId;
    }
    public Integer getStatusId() {
        return statusId;
    }
    public Date getRegister() {
        return  register;
    }
    public List<Objective> getObjectives() {
        return objectives;
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
    public void setApplicationType(Integer applicationType) {
        this.applicationType = applicationType;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    public void setRegister(Date register) {
        this.register = register;
    }
    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }

}