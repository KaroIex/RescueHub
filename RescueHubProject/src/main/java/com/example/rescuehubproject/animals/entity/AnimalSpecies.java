package com.example.rescuehubproject.animals.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ANIMALSPECIES")
public class AnimalSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_species", nullable = false)
    private Long id;


    @Column(name = "species_name", nullable = false, unique = true)
    private String speciesName;

    @OneToMany(mappedBy = "animalSpecies") // relacja jeden-do-wielu z tabelą Animals
    private Set<Animal> animals;
}