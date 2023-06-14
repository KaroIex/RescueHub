package com.example.rescuehubproject.adopters.controllers;

import com.example.rescuehubproject.adopters.dto.GetAdopterByIdDTO;
import com.example.rescuehubproject.adopters.dto.GetAdopterDTO;
import com.example.rescuehubproject.adopters.services.AdopterService;
import com.example.rescuehubproject.adoption.dto.AdoptionFormDTO;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@Tag(name = "Adopters", description = "Endpoints for managing adopters")
@RequestMapping("/api/adopters")
public class AdopterController {

    @Autowired
    private AdopterService adopterService;

    @GetMapping
    @Operation(summary = "Find all adopters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of adopters",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAdopterDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<List<GetAdopterDTO>> getAllAdopters(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "email") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
            @RequestParam(name = "filter", required = false, defaultValue = "") String filter) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
            Page<GetAdopterDTO> adopterDTOPage = adopterService.findAll(pageable, filter);
            return new ResponseEntity<>(adopterDTOPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find adopter by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved adopter",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAdopterByIdDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Adopter not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<GetAdopterByIdDTO> getAdopterById(@PathVariable Long id) {
        GetAdopterByIdDTO adopterByIdDTO = adopterService.findById(id);

        if (adopterByIdDTO != null) {
            return ResponseEntity.ok(adopterByIdDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
        adopterService.adopt(authentication, animalId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/{adopterId}/matchAnimal")
    @Operation(summary = "Match adopter to animal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully matched adopter to animal",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AnimalDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<Map.Entry<AnimalDTO, Double>>> matchAdopterToAnimal(@PathVariable Long adopterId,
                                                                                   @RequestBody AdoptionFormDTO form,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "value"));
        Page<Map.Entry<AnimalDTO, Double>> result = adopterService.matchAdopterToAnimal(form, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

