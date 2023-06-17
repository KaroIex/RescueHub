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

    @NotEmpty(message = "Needs attention must not be empty")
    private boolean needsAttention;

    @NotEmpty(message = "Is social animal must not be empty")
    private boolean isSocialAnimal;

    @NotEmpty(message = "Needs outdoor space must not be empty")
    private boolean needsOutdoorSpace;

    @NotEmpty(message = "Good with children must not be empty")
    private boolean GoodWithChildren;


    public AnimalDTO() {
    }

    public AnimalDTO(String name, Integer age, String animalSpecies, String description, boolean needsAttention, boolean isSocialAnimal, boolean needsOutdoorSpace, boolean goodWithChildren) {
        this.name = name;
        this.age = age;
        this.animalSpecies = animalSpecies;
        this.description = description;
        this.needsAttention = needsAttention;
        this.isSocialAnimal = isSocialAnimal;
        this.needsOutdoorSpace = needsOutdoorSpace;
        GoodWithChildren = goodWithChildren;
    }

    @Override
    public String toString() {
        return
                "name='" + name + '\'' +
                        ", animalSpecies='" + animalSpecies + '\'';

    }


}
