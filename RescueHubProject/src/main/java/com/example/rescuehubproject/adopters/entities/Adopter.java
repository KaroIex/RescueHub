package com.example.rescuehubproject.adopters.entities;

import com.example.rescuehubproject.accounts.entity.Person;


import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.adoption.entity.Adoption;
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
public class Adopter extends User { // rozszerza klasę Person


    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "adopter") // relacja jeden-do-wielu z tabelą Adoptions
    private Set<Adoption> adoptions;
}