package com.example.rescuehubproject.accounts.entity;

import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "USERS")
public class User extends Person { // rozszerza klasę Person

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "password required")
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles")
    private List<Role> roles;


    public void addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);

        if (role == Role.ROLE_ADOPTER && adopter == null) {
            // Tworzy pusty rekord danych adoptera przy nadaniu roli ADOPTER
            Adopter newAdopter = new Adopter();
            newAdopter.setUser(this);
            setAdopter(newAdopter);
        }
    }

    public void removeRole(Role role) {
        roles.remove(role);

        if (role == Role.ROLE_ADOPTER && adopter != null) {
            // Usuwa rekord danych adoptera przy zabraniu roli ADOPTER
            adopter.setUser(null);
            setAdopter(null);
        }
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Adopter adopter;

    public Adopter getAdopter() {
        return adopter;
    }

    public void setAdopter(Adopter adopter) {
        this.adopter = adopter;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return this.getId() != null && Objects.equals(this.getId(), user.getId());
    }

    @Override
    public Long getId() {
        return super.getId();
    }
}