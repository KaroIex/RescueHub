package com.example.rescuehubproject.adopters.entities;

import com.example.rescuehubproject.accounts.entity.Person;


import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.adoption.entity.Adoption;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ADOPTERS")
public class Adopter {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_adopter", nullable = false)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "adopter") // relacja jeden-do-wielu z tabelą Adoptions
    private Set<Adoption> adoptions;

//    @OneToOne(mappedBy = "adopter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Preference adopterPreferences;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_person")
    private User user;
}