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

@RestController
@RequestMapping("/api/animal")
@Tag(name = "Animals", description = "Endpoints for managing animals")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping
    @Operation(summary = "Find all animals", description = "Returns a list of AnimalsWithIdDTO objects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of animals",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })
    public List<AnimalDTO> findAll(){
        return animalService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find animal by ID", description = "Returns an AnimalDTO object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the animal",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Animal not found",
                    content = @Content) })
    public ResponseEntity<AnimalDTO> findById(@PathVariable Long id){
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        return animalDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    @Operation(summary = "Create a new animal", description = "Saves a new AnimalDTO object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the animal",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content) })
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
            AnimalDTO updatedAnimal = animalService.update(id, updatedAnimalDTO);
            return ResponseEntity.ok(updatedAnimal);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete animal", description = "Deletes an AnimalDTO object by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the animal",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Animal not found",
                    content = @Content) })
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
