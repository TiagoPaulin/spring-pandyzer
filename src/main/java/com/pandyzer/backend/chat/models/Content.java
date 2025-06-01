package com.pandyzer.backend.chat.models;

import java.util.List;

public class Content {

    private String role;
    private List<Part> parts;

    public Content (String role, List<Part> parts) {

        this.role = role;
        this.parts = parts;

    }

    public String getRole() {
        return role;
    }
    public List<Part> getParts() {
        return parts;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

}

