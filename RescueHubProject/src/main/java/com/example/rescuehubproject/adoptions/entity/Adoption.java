package com.example.rescuehubproject.adoptions.entity;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.animals.entity.Animal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    @JoinColumn(name = "id_user", nullable = false) // relacja wiele-do-jednego z tabelą Users
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_adopter", nullable = false) // relacja wiele-do-jednego z tabelą Adopters
    private Adopter adopter;

    @ManyToOne
    @JoinColumn(name = "id_animal", nullable = false) // relacja wiele-do-jednego z tabelą Animals
    private Animal animal;
}