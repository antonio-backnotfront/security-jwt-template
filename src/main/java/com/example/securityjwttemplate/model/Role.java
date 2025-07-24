package com.example.securityjwttemplate.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "Role")

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(targetEntity = User.class, cascade = CascadeType.REMOVE, mappedBy = "role", orphanRemoval = true)
    private Set<User> user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
