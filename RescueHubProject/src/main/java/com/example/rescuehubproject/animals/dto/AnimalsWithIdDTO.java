package com.example.rescuehubproject.animals.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalsWithIdDTO extends AnimalDTO {

    private Long id;

    public AnimalsWithIdDTO(long l, String name, Integer age, String animalSpecies, String description, boolean needsAttention, boolean isSocialAnimal, boolean needsOutdoorSpace, boolean goodWithChildren) {
        super(name, age, animalSpecies, description, needsAttention, isSocialAnimal, needsOutdoorSpace, goodWithChildren);
        this.id = l;
    }

    public AnimalsWithIdDTO() {
    }
}
