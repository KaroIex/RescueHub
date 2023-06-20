package com.example.rescuehubproject.accounts.entity;

import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "users")
@Schema(name = "User", description = "User entity")
public class User {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_person", nullable = false)
    @Schema(description = "User id", example = "1")
    private Long id;

    @NotEmpty(message = "name required")
    @Column(name = "name", nullable = false)
    @Schema(description = "User name", example = "John")
    private String name;

    @NotEmpty(message = "lastname required")
    @Column(name = "lastname", nullable = false)
    @Schema(description = "User lastname", example = "Doe")
    private String lastname;

    @NotEmpty(message = "email required")
    @Column(name = "email", nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Schema(description = "User email", example = "example@gmail")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "password required")
    @Column(name = "password", nullable = false)
    @Schema(description = "User password", example = "password!QAZ")
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles")
    @Column(name = "role")
    private List<Role> roles;

    public User(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public void addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);

    }

    public void removeRole(Role role) {
        roles.remove(role);

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
    public int hashCode() {
        return getClass().hashCode();
    }
}