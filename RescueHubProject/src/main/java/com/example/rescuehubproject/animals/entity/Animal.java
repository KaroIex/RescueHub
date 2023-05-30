package com.example.rescuehubproject.animals.entity;


import com.example.rescuehubproject.adoptions.entity.Adoption;
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
@Table(name = "ANIMALS")
public class Animal {

    //napisz mi co tu sie dzieje
    // -
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_animal", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_species", nullable = false) // relacja wiele-do-jednego z tabelą AnimalSpecies
    private AnimalSpecies animalSpecies;

    @OneToMany(mappedBy = "animal") // relacja jeden-do-wielu z tabelą Adoptions
    private Set<Adoption> adoptions;
}