package com.example.rescuehubproject.accounts.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Person {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_person", nullable = false)
    private Long id;

    @NotEmpty(message = "name required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty(message = "lastname required")
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotEmpty(message = "email required")
    @Column(name = "email", nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    public Person(String name, String lastname, String email) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}