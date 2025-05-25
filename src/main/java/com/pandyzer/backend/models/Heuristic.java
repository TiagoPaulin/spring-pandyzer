package com.pandyzer.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Heuristica")
public class Heuristic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "heuristicaId")
    private Long id;
    @Column(name = "descricao")
    private String description;
    @Column(name = "data_cadastro")
    private Date register;

    public Heuristic () {}

    public Heuristic (Long id, String description, Date register) {

        this.id = id;
        this.description = description;
        this.register = register;

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

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setRegister(Date register) {
        this.register = register;
    }

}
