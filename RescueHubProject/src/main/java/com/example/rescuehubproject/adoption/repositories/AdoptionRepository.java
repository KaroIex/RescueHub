package com.example.rescuehubproject.adoption.repositories;

import com.example.rescuehubproject.adoption.entity.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

}
