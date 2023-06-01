package com.example.rescuehubproject.adopters.controllers;

import com.example.rescuehubproject.adopters.dto.AdopterDTO;
import com.example.rescuehubproject.adopters.services.AdopterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/adopters")
public class AdopterController {

    @Autowired
    private AdopterService adopterService;

    @GetMapping
    public List<AdopterDTO> findAll() {
        return adopterService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdopterDTO> findById(@PathVariable Long id) {
        Optional<AdopterDTO> adopterDTO = adopterService.findById(id);
        return adopterDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public AdopterDTO save(@RequestBody AdopterDTO adopterDTO) {
        return adopterService.save(adopterDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdopterDTO> update(@PathVariable Long id, @RequestBody AdopterDTO updatedAdopterDTO) {
        Optional<AdopterDTO> adopterDTO = adopterService.findById(id);
        if (adopterDTO.isPresent()) {
            AdopterDTO updatedAdopter = adopterService.save(updatedAdopterDTO);
            return ResponseEntity.ok(updatedAdopter);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<AdopterDTO> adopterDTO = adopterService.findById(id);
        if (adopterDTO.isPresent()) {
            adopterService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}