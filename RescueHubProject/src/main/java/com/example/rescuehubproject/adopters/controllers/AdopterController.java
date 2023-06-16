package com.example.rescuehubproject.adopters.controllers;

import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.adopters.dto.GetAdopterByIdDTO;
import com.example.rescuehubproject.adopters.dto.GetAdopterDTO;
import com.example.rescuehubproject.adopters.dto.PutAdopterDTO;
import com.example.rescuehubproject.adopters.repositories.AdopterRepository;
import com.example.rescuehubproject.adopters.services.AdopterService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "Adopters", description = "Endpoints for managing adopters")
@RequestMapping("/api/adopters")
public class AdopterController {

    @Autowired
    private AdopterService adopterService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdopterRepository adopterRepository;

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


    @PutMapping("/{userId}")
    @Operation(summary = "Update adopter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated adopter",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PutAdopterDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Adopter not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<PutAdopterDTO> updateAdopter(@PathVariable("userId") Long userId, @Parameter(description = "Adopter data to update")
    @RequestBody PutAdopterDTO adopterDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(adopterService.updateAdopter(authentication, adopterDto));

    }
}

