package com.example.rescuehubproject.animals.controllers;

import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@RequestMapping("/api/animal")
@Tag(name = "Animals", description = "Endpoints for managing animals")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping
    public List<AnimalDTO> findAll(){
        return animalService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> findById(@PathVariable Long id){
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        return animalDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    public AnimalDTO save(@RequestBody AnimalDTO animalDTO) throws NoSuchFieldException {
        return animalService.save(animalDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update animal", description = "Updates an existing AnimalDTO object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the animal",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Animal not found",
                    content = @Content) })
    public ResponseEntity<AnimalDTO> update(@PathVariable Long id, @RequestBody AnimalDTO updatedAnimalDTO) throws NoSuchFieldException {
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        if(animalDTO.isPresent()) {
            AnimalDTO updatedAnimal = animalService.update(id, updatedAnimalDTO);            return ResponseEntity.ok(updatedAnimal);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws NoSuchFieldException {
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        if(animalDTO.isPresent()){
            animalService.deleteById(id);
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
