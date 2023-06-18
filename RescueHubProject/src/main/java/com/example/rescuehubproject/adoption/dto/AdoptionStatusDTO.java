package com.example.rescuehubproject.adoption.dto;

import com.example.rescuehubproject.animals.dto.AnimalSpeciesDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdoptionStatusDTO extends AdoptionDTO {

    @NotEmpty(message = "Status must not be empty")
    private String status;

    public AdoptionStatusDTO(String adoptername, String animalName){
        super(adoptername, animalName);
        this.status = status;
    }

    AdoptionStatusDTO(){}
}
