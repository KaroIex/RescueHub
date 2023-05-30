package com.example.rescuehubproject.accounts.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass // oznacza klasę, której atrybuty można odziedziczyć
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