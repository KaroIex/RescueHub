package com.example.rescuehubproject.animals.entity;


import com.example.rescuehubproject.adoption.entity.Adoption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ANIMALS")
@Schema(name = "Animal", description = "Entity representing an animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_animal", nullable = false)
    @Schema(description = "Animal id", example = "1")
    private Long id;

    @Column(name = "needs_attention", nullable = false)
    @Schema(description = "Needs attention", example = "true")
    private boolean needsAttention;

    @Column(name = "is_social_animal", nullable = false)
    @Schema(description = "Is social animal", example = "true")
    private boolean isSocialAnimal;

    @Column(name = "needs_outdoor_space", nullable = false)
    @Schema(description = "Needs outdoor space", example = "true")
    private boolean needsOutdoorSpace;

    @Column(name = "good_with_children", nullable = false)
    @Schema(description = "Good with children", example = "true")
    private boolean GoodWithChildren;
    @Column(name = "name", nullable = false)
    @Schema(description = "Name of the animal", example = "Burek")
    private String name;

    @Column(name = "age", nullable = false)
    @Schema(description = "Age of the animal", example = "2")
    private Integer age;

    @Column(name = "description", nullable = false)
    @Schema(description = "Description of the animal", example = "Burek is a very nice dog")
    private String description;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "id_species", nullable = false) // relacja wiele-do-jednego z tabelą AnimalSpecies
    @Schema(description = "Species of the animal", example = "Dog")
    private AnimalSpecies animalSpecies;

    @OneToMany(mappedBy = "animal", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Schema(description = "Adoptions of the animal")
    private Set<Adoption> adoptions;

    public void addAdoption(Adoption adoption) {
        if (adoptions.isEmpty()) {
            adoptions = new HashSet<>();
        }
        adoptions.add(adoption);
        adoption.setAnimal(this);
    }

    public void removeAdoption(Adoption adoption) {
        if (adoptions.isEmpty()) {
        }
        else {
            adoption.setAnimal(null);
            adoptions.remove(adoption);
        }
    }
}