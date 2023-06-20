package com.example.rescuehubproject.animals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AnimalDTO", description = "DTO representing an animal")
public class AnimalDTO {

    @NotEmpty(message = "Name must not be empty")
    @Schema(description = "Name of the animal", example = "Burek")
    private String name;

    @NotEmpty(message = "Age must not be empty")
    @Schema(description = "Age of the animal", example = "2")
    private Integer age;

    @NotEmpty(message = "Species id must not be empty")
    @Schema(description = "Species of the animal", example = "Dog")
    private String animalSpecies;

    @NotEmpty(message = "Description id must not be empty")
    @Schema(description = "Description of the animal", example = "Burek is a very nice dog")
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
