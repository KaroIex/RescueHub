package com.example.rescuehubproject.animals.entity;


import com.example.rescuehubproject.adoption.entity.Adoption;
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
public class Animal {

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

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_species", nullable = false) // relacja wiele-do-jednego z tabelą AnimalSpecies
    private AnimalSpecies animalSpecies;

    @OneToMany(mappedBy = "animal", cascade ={CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH} ) // relacja jeden-do-wielu z tabelą Adoptions
    private Set<Adoption> adoptions;

    public void addAdoption(Adoption adoption){
        if(adoptions.isEmpty()){
            adoptions = new HashSet<>();
        }
        adoptions.add(adoption);
        adoption.setAnimal(this);
    }

    public void removeAdoption(Adoption adoption){
        if(adoptions.isEmpty())
            return;
        else {
            adoption.setAnimal(null);
            adoptions.remove(adoption);
        }
    }
}