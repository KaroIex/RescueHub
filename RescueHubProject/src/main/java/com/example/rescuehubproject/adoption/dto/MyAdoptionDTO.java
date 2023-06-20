package com.example.rescuehubproject.adoption.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Adoption Data Transfer Object")
@Getter
@Setter
public class MyAdoptionDTO {

    @Schema(description = "Adoption's id", example = "1")
    private Long id;

    @Schema(description = "Adopter's id", example = "1")
    private Long adopterId;

    @Schema(description = "Animal's id", example = "1")
    private Long animalId;

    @Schema(description = "Animal's name", example = "Bobby")
    private String animalName;

    @Schema(description = "Animal's species", example = "Dog")
    private String animalSpecies;


}
