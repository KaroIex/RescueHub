package com.example.rescuehubproject.adopters.repositories;

import com.example.rescuehubproject.adopters.entities.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

}
