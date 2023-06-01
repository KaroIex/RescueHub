package com.example.rescuehubproject.adoption.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdoptionDTO {

    @NotEmpty(message = "Adopter's name must not be empty")
    private String adopterName;

    @NotEmpty(message = "Animal's name must not be empty")
    private String animalName;

}
