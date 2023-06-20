package com.example.rescuehubproject.adoption.repositories;

import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adoption.entity.Adoption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    Page<Adoption> findAllByAdopter(Adopter adopter, Pageable pageable);
}
