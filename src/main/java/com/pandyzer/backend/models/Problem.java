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

    @Column(name = "descricao")
    private String description;

    @Column(name = "recomendacao")
    private String recomendation;

    @Column(name = "data_cadastro")
    @Temporal(TemporalType.TIMESTAMP)
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

    // NOVO: imagem em Base64 (sem ou com prefixo data URI)
    @Lob
    @Column(name = "imagem_base64", columnDefinition = "LONGTEXT")
    private String imageBase64;

    public Problem() {}

    public Problem(Long id, User user, String description, String recomendation,
                   Date register, Objective objective, Heuristic heuristic, Severity severity,
                   String imageBase64) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.recomendation = recomendation;
        this.register = register;
        this.objective = objective;
        this.heuristic = heuristic;
        this.severity = severity;
        this.imageBase64 = imageBase64;
    }

    // getters
    public Long getId() { return id; }
    public String getDescription() { return description; }
    public String getRecomendation() { return recomendation; }
    public Date getRegister() { return register; }
    public Objective getObjective() { return objective; }
    public Heuristic getHeuristic() { return heuristic; }
    public Severity getSeverity() { return severity; }
    public User getUser() { return user; }
    public String getImageBase64() { return imageBase64; }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setRecomendation(String recomendation) { this.recomendation = recomendation; }
    public void setRegister(Date register) { this.register = register; }
    public void setObjective(Objective objective) { this.objective = objective; }
    public void setHeuristic(Heuristic heuristic) { this.heuristic = heuristic; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public void setUser(User user) { this.user = user; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
