package com.example.rescuehubproject.animals.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalSpeciesWithIdDTO extends AnimalSpeciesDTO {

    private Long id;

    public AnimalSpeciesWithIdDTO(long l, String cat) {
        super(cat);
        this.id = l;
    }

    public AnimalSpeciesWithIdDTO() {
    }
}