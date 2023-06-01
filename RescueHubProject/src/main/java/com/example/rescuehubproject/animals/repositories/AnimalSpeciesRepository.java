package com.example.rescuehubproject.animals.repositories;

import com.example.rescuehubproject.animals.entity.AnimalSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalSpeciesRepository extends JpaRepository<AnimalSpecies, Long> {
}