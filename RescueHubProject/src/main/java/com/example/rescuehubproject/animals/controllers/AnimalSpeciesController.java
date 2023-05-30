package com.example.rescuehubproject.animals.controllers;

import com.example.rescuehubproject.animals.dto.AnimalSpeciesDTO;
import com.example.rescuehubproject.animals.services.AnimalSpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animalspecies")
public class AnimalSpeciesController {

    @Autowired
    private AnimalSpeciesService animalSpeciesService;


    @GetMapping
    public List<AnimalSpeciesDTO> findAll() {
        return animalSpeciesService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalSpeciesDTO> findById(@PathVariable Long id) {
        Optional<AnimalSpeciesDTO> animalSpeciesDTO = animalSpeciesService.findById(id);
        return animalSpeciesDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public AnimalSpeciesDTO save(@RequestBody AnimalSpeciesDTO animalSpeciesDTO) {
        return animalSpeciesService.save(animalSpeciesDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalSpeciesDTO> update(@PathVariable Long id, @RequestBody AnimalSpeciesDTO updatedAnimalSpeciesDTO) {
        Optional<AnimalSpeciesDTO> animalSpeciesDTO = animalSpeciesService.findById(id);
        if (animalSpeciesDTO.isPresent()) {
            AnimalSpeciesDTO updatedAnimalSpecies = animalSpeciesService.save(updatedAnimalSpeciesDTO);
            return ResponseEntity.ok(updatedAnimalSpecies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<AnimalSpeciesDTO> animalSpeciesDTO = animalSpeciesService.findById(id);
        if (animalSpeciesDTO.isPresent()) {
            animalSpeciesService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}