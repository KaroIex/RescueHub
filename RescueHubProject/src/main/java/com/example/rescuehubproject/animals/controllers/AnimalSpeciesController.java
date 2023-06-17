package com.example.rescuehubproject.animals.controllers;

import com.example.rescuehubproject.animals.dto.AnimalSpeciesDTO;
import com.example.rescuehubproject.animals.services.AnimalSpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.exception.ConstraintViolationException;



import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/animalspecies")
@Tag(name = "Animal Species", description = "Endpoints for managing animal species")
public class AnimalSpeciesController {

    @Autowired
    private AnimalSpeciesService animalSpeciesService;


    @GetMapping
    @Operation(summary = "Find all animal species", description = "Returns a list of AnimalSpeciesWithIdDTO objects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of animal species",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalSpeciesWithIdDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })
    public ResponseEntity<List<AnimalSpeciesWithIdDTO>> findAll(
            @Parameter(description = "Page number, starting from 0")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Number of elements per page")
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @Parameter(description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
            @RequestParam(name = "sort", required = false, defaultValue= "speciesName") String sortBy,
            @Parameter(description = "Sorting order in the format: asc|desc. Default sort order is ascending.")
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
    @Operation(summary = "Find animal species by ID", description = "Returns an AnimalSpeciesDTO object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the animal species",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalSpeciesDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Animal species not found",
                    content = @Content) })
    public ResponseEntity<AnimalSpeciesDTO> findById(@PathVariable Long id) {
        Optional<AnimalSpeciesDTO> animalSpeciesDTO = animalSpeciesService.findById(id);
        return animalSpeciesDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    @Operation(summary = "Create a new animal species", description = "Saves a new AnimalSpeciesDTO object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the animal species",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalSpeciesDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content) })
    public AnimalSpeciesDTO save(@RequestBody AnimalSpeciesDTO animalSpeciesDTO) {
        return animalSpeciesService.save(animalSpeciesDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update animal species", description = "Updates an existing AnimalSpeciesDTO object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the animal species",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnimalSpeciesDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Animal species not found",
                    content = @Content) })
    public ResponseEntity<AnimalSpeciesDTO> update(@PathVariable Long id, @RequestBody AnimalSpeciesDTO updatedAnimalSpeciesDTO) {
        Optional<AnimalSpeciesDTO> animalSpeciesDTO = animalSpeciesService.findById(id);
        if (animalSpeciesDTO.isPresent()) {
            AnimalSpeciesDTO updatedAnimalSpecies = animalSpeciesService.update(id, updatedAnimalSpeciesDTO); // Pass the id to update method
            return ResponseEntity.ok(updatedAnimalSpecies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete animal species", description = "Deletes an AnimalSpeciesDTO object by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the animal species",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Animal species not found",
                    content = @Content) })
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
    @ApiResponse(responseCode = "409", description = "Species name already exists",
            content = @Content)
    public ResponseEntity<Object> handleException(Exception ex) {
        String errorMessage = "There is already an animal species with the provided speciesName";
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }


}