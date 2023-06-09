package com.example.rescuehubproject.adoption.controllers;

import com.example.rescuehubproject.adoption.dto.AdoptionDTO;
import com.example.rescuehubproject.adoption.services.AdoptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/api/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @GetMapping
    public List<AdoptionDTO> findAll() {
        return adoptionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionDTO> findById(@PathVariable Long id) {
        Optional<AdoptionDTO> adoptionDTO = adoptionService.findById(id);
        return adoptionDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public AdoptionDTO save(@RequestBody AdoptionDTO adoptionDTO) {
        return adoptionService.save(adoptionDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionDTO> update(@PathVariable Long id, @RequestBody AdoptionDTO updatedAdoptionDTO) {
        Optional<AdoptionDTO> adoptionDTO = adoptionService.findById(id);
        if (adoptionDTO.isPresent()) {
            AdoptionDTO updatedAdoption = adoptionService.save(updatedAdoptionDTO);
            return ResponseEntity.ok(updatedAdoption);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<AdoptionDTO> adoptionDTO = adoptionService.findById(id);
        if (adoptionDTO.isPresent()) {
            adoptionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
