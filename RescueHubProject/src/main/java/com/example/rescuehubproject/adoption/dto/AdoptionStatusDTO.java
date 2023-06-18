package com.example.rescuehubproject.adoption.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdoptionStatusDTO {

    @NotEmpty(message = "Adopter's name must not be empty")
    @Schema(description = "Adopter's name", example = "John Doe")
    private String adopterName;

    @NotEmpty(message = "Animal's name must not be empty")
    @Schema(description = "Animal's name", example = "Bobby")
    private String animalName;

    @NotEmpty(message = "Status must not be empty")
    private String Status;
}
