package com.example.rescuehubproject.animals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(name = "AnimalSpeciesDTO", description = "DTO representing an animal species")
public class AnimalSpeciesDTO {

    @Schema(description = "Species name", example = "Dog")
    @NotEmpty(message = "Species name must not be empty")
    private String speciesName;

    public AnimalSpeciesDTO(String speciesName) {
        this.speciesName = speciesName;
    }

    public AnimalSpeciesDTO() {

    }


}

