/*package com.example.rescuehubproject.adoptions.entity;

import com.example.rescuehubproject.accounts.entity.Person;


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
public class Adopter extends Person { // rozszerza klasę Person

    @NotEmpty(message = "phone required")
    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToMany(mappedBy = "adopter") // relacja jeden-do-wielu z tabelą Adoptions
    private Set<Adoption> adoptions;
}
*/