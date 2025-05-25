package com.pandyzer.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userTypeId")
    private Long id;
    @Column(name = "descricao")
    private String description;
    @OneToMany(mappedBy = "userType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public UserType () {}

    public UserType (Long id, String description) {

        this.id = id;
        this.description = description;

    }

    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public List<User> getUsers() {
        return users;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }

}
