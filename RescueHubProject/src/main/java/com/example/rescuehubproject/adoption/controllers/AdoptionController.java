package com.example.rescuehubproject.adoption.controllers;

import com.example.rescuehubproject.adopters.services.AdopterService;
import com.example.rescuehubproject.adoption.dto.AdoptionDTO;
import com.example.rescuehubproject.adoption.dto.AdoptionFormDTO;
import com.example.rescuehubproject.adoption.services.AdoptionService;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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


    @PostMapping("/adopt/{animalId}")
    @Operation(summary = "Adopt an animal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully adopted animal",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Animal not found or Animal already adopted or User not found or User is not an adopter.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> adopt(@RequestParam(name = "id animal", required = true) @PathVariable Long animalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        adoptionService.adopt(authentication, animalId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/matchAnimal")
    @Operation(summary = "Match adopter to animal", description = "Returns a map where key is a string representation of AnimalDTO and value is the match rating as double.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully matched adopter to animal",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<Map.Entry<AnimalDTO, Double>>> matchAdopterToAnimal(@RequestBody AdoptionFormDTO form,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "value"));
        var result = adoptionService.matchAdopterToAnimal(form, pageable).getContent();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
