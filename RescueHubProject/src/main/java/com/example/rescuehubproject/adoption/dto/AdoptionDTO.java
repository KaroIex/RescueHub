package com.example.rescuehubproject.adoption.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter
@Setter
@Schema(description = "Adoption Data Transfer Object")
public class AdoptionDTO {


    @NotEmpty(message = "Adopter's name must not be empty")
    @Schema(description = "Adopter's name", example = "John Doe")
    private String adopterName;

    @NotEmpty(message = "Animal's name must not be empty")
    @Schema(description = "Animal's name", example = "Bobby")
    private String animalName;

}
