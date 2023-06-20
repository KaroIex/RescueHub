package com.example.rescuehubproject.animals.controllers;

import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.dto.AnimalsWithIdDTO;
import com.example.rescuehubproject.animals.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)})
    public ResponseEntity<List<AnimalsWithIdDTO>> findAll(
            @Parameter(description = "Page number, starting from 0")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Number of elements per page")
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @Parameter(description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
            @RequestParam(name = "sort", required = false, defaultValue = "animalSpecies") String sortBy,
            @Parameter(description = "Sorting order in the format: asc|desc. Default sort order is ascending.")
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
            @Parameter(description = "Filter criteria in the format: property[.operation]:value. Example: name.eq=Zoe")
            @RequestParam(name = "filter", required = false, defaultValue = "") String filter) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
            Page<AnimalsWithIdDTO> animalsWithIdDTOPage = animalService.findAll(pageable, filter);
            return new ResponseEntity<>(animalsWithIdDTOPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find animal by ID", description = "Returns an AnimalDTO object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the animal",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Animal not found",
                    content = @Content)})
    public ResponseEntity<AnimalDTO> findById(@PathVariable Long id) throws NoSuchFieldException {
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        return animalDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    @Operation(summary = "Create a new animal", description = "Saves a new AnimalDTO object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the animal",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)})
    public AnimalDTO save(@RequestBody AnimalDTO animalDTO) throws NoSuchFieldException {
        return animalService.save(animalDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update animal", description = "Updates an existing AnimalDTO object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the animal",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Animal not found",
                    content = @Content)})
    public ResponseEntity<AnimalDTO> update(@PathVariable Long id, @RequestBody AnimalDTO updatedAnimalDTO) throws NoSuchFieldException {
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        if (animalDTO.isPresent()) {
            AnimalDTO updatedAnimal = animalService.update(id, updatedAnimalDTO);
            return ResponseEntity.ok(updatedAnimal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete animal", description = "Deletes an AnimalDTO object by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the animal",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Animal not found",
                    content = @Content)})
    public ResponseEntity<Void> delete(@PathVariable Long id) throws NoSuchFieldException {
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        if (animalDTO.isPresent()) {
            animalService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
