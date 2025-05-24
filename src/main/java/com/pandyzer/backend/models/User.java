package com.pandyzer.backend.models;

import jakarta.persistence.*;

import java.sql.Date;

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
    @Column(name = "tipoUsuario")
    private Integer userType;
    @Column(name = "data_cadastro")
    private Date register;

    public User () {}

    public User (Long id, String name, String email, String password, Integer active, Integer userType, Date register) {

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
    public Integer getUserType() {
        return userType;
    }
    public Date getRegister() {
        return register;
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
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    public void setRegister(Date register) {
        this.register = register;
    }

}
