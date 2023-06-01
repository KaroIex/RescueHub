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

    @NotEmpty(message = "Species name must not be empty")
    private String speciesName;

}
