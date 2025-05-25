package com.pandyzer.backend.models;

import jakarta.persistence.*;

@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "statusId")
    private Long id;
    @Column(name = "descricao")
    private String description;

    public Status () {}

    public Status (Long id, String description) {

        this.id = id;
        this.description = description;

    }

    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
