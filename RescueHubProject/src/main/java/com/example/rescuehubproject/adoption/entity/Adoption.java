package com.example.rescuehubproject.adoption.entity;

import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adoption.util.Status;
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
    @Column(name = "id_adoption", nullable = false)
    private Long id;

    @Column(name = "adoption_date", nullable = false)
    private LocalDate adoptionDate;

    @ManyToOne
    @JoinColumn(name = "id_adopter", nullable = false) // relacja wiele-do-jednego z tabelą Adopters
    private Adopter adopter;

    @ManyToOne
    @JoinColumn(name = "id_animal", nullable = false) // relacja wiele-do-jednego z tabelą Animals
    private Animal animal;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private Status status;
}