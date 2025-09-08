package com.pandyzer.backend.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JoinColumn(name = "tipoAplicacaoId", nullable = false)
    private ApplicationType applicationType;
    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private User user;
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Objective> objectives = new ArrayList<>();
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Evaluator> evaluators = new ArrayList<>();
    @Column(name = "publico")
    @JsonProperty("isPublic")
    @JsonAlias({"public"})
    private Boolean isPublic = false;
    @Column(name = "limite_avaliadores")
    private Integer evaluatorsLimit;

    public Evaluation () {}

    public Evaluation (Long id, String description, Date startDate, Date finalDate, String link, ApplicationType applicationType, Date register, User user, boolean isPublic, Integer evaluatorsLimit) {

        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.link = link;
        this.applicationType = applicationType;
        this.register = register;
        this.user = user;
        this.isPublic = isPublic;
        this.evaluatorsLimit = evaluatorsLimit;
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
    public Date getRegister() {
        return  register;
    }
    public List<Objective> getObjectives() {
        return objectives;
    }
    public User getUser() {
        return user;
    }
    public List<Evaluator> getEvaluators() {
        return evaluators;
    }
    public Integer getEvaluatorsLimit() {
        return evaluatorsLimit;
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
    public void setRegister(Date register) {
        this.register = register;
    }
    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setEvaluators(List<Evaluator> evaluators) {
        this.evaluators = evaluators;
    }

    @JsonProperty("isPublic")
    public Boolean getIsPublic() {
        return isPublic;
    }

    @JsonProperty("isPublic")
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    @JsonIgnore
    public Boolean getPublic() {
        return isPublic;
    }

    @JsonIgnore
    public void setPublic(Boolean aPublic) {
        this.isPublic = aPublic;
    }

    public void setEvaluatorsLimit(Integer evaluatorsLimit) {
        this.evaluatorsLimit = evaluatorsLimit;
    }
}