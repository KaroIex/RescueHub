package com.example.rescuehubproject.animals.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ANIMALSPECIES")
@Schema(name = "AnimalSpecies", description = "Entity representing an animal species")
public class AnimalSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_species", nullable = false)
    @Schema(description = "Species id", example = "1")
    private Long id;


    @Column(name = "species_name", nullable = false, unique = true)
    @Schema(description = "Species name", example = "Dog")
    private String speciesName;

    @OneToMany(mappedBy = "animalSpecies", cascade = CascadeType.ALL) // relacja jeden-do-wielu z tabelą Animals
    @Schema(description = "Animals of the species")
    private List<Animal> animals = new ArrayList<>();

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal(Animal animal) {
        if (animals.isEmpty()) {
        }
        else {
            animals.remove(animal);
        }
    }
}