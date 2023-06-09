package com.example.rescuehubproject.animals.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalDTO {

    @NotEmpty(message = "Name must not be empty")
    private String name;

    @NotEmpty(message = "Age must not be empty")
    private Integer age;

    @NotEmpty(message = "Species id must not be empty")
    private String animalSpecies;

    @NotEmpty(message = "Description id must not be empty")
    private String description;

    public AnimalDTO(String name, Integer age, String animalSpecies, String description) {
        this.name = name;
        this.age = age;
        this.animalSpecies = animalSpecies;
        this.description = description;
    }

    public AnimalDTO() {
    }

}
