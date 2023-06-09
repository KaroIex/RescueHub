package com.example.rescuehubproject.adopters.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AdopterDTO {


    @NotEmpty(message = "First name must not be empty")
    private String firstName;

    @NotEmpty(message = "Last name must not be empty")
    private String lastName;

    @NotEmpty(message = "Email must not be empty")
    private String email;

    @NotEmpty(message = "Phone number must not be empty")
    private String phoneNumber;

}
