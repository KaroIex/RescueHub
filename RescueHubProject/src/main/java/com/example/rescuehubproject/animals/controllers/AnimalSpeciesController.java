package com.example.rescuehubproject.animals.controllers;

import com.example.rescuehubproject.animals.dto.AnimalSpeciesDTO;
import com.example.rescuehubproject.animals.services.AnimalSpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.rescuehubproject.animals.dto.AnimalSpeciesWithIdDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/animalspecies")
public class AnimalSpeciesController {

    @Autowired
    private AnimalSpeciesService animalSpeciesService;


    @GetMapping
    public ResponseEntity<List<AnimalSpeciesWithIdDTO>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "speciesName") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
            @RequestParam(name = "filter", required = false, defaultValue = "") String filter) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
            Page<AnimalSpeciesWithIdDTO> animalSpeciesWithIdDTOPage = animalSpeciesService.findAll(pageable, filter);
            return new ResponseEntity<>(animalSpeciesWithIdDTOPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    

    @ExceptionHandler({DataIntegrityViolationException.class, NoSuchElementException.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}