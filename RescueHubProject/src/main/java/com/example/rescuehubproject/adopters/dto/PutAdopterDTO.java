package com.example.rescuehubproject.adopters.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "PutAdopterDto", description = "DTO for putting adopter")
public class PutAdopterDTO {


    @Schema(description = "Adopter's phone number", example = "1234567890")
    @NotEmpty(message = "Phone number cannot be empty")
    private String phone;

    @Schema(description = "Adopter's street", example = "Street 1")
    @NotEmpty(message = "Street cannot be empty")
    private String street;

    @Schema(description = "Adopter's city", example = "City")
    @NotEmpty(message = "City cannot be empty")
    private String city;

    @Schema(description = "Adopter's state", example = "State")
    @NotEmpty(message = "State cannot be empty")
    private String state;

    @Schema(description = "Adopter's zip", example = "12345")
    @NotEmpty(message = "Zip cannot be empty")
    private String zip;

    @Schema(description = "Adopter's country", example = "Country")
    @NotEmpty(message = "Country cannot be empty")
    private String country;
}