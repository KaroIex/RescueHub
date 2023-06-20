package com.example.rescuehubproject.animals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AnimalSpeciesWithIdDTO", description = "DTO representing an animal species with id")
public class AnimalSpeciesWithIdDTO extends AnimalSpeciesDTO {

    @Schema(description = "Species id", example = "1")
    private Long id;

    public AnimalSpeciesWithIdDTO(long l, String cat) {
        super(cat);
        this.id = l;
    }

    public AnimalSpeciesWithIdDTO() {
    }
}