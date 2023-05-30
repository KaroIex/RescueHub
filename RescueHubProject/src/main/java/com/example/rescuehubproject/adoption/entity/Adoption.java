package com.example.rescuehubproject.adoption.entity;

import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.animals.entity.Animal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ADOPTIONS")
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adopter_id", nullable = false)
    private Adopter adopter;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Column(name = "adoption_date", nullable = false)
    private LocalDate adoptionDate;

}