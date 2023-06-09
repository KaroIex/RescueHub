package com.example.rescuehubproject.animals.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AnimalSpeciesDTO {

    @NotEmpty(message = "Species name must not be empty")
    private String speciesName;

    public AnimalSpeciesDTO(String speciesName) {
        this.speciesName = speciesName;
    }

    public AnimalSpeciesDTO() {

    }


}

