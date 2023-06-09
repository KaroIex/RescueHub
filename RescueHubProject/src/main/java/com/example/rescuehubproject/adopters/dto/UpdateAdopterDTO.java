package com.example.rescuehubproject.adopters.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Getter
@Setter
@Schema(description = "Update Adopter Data Transfer Object")
public class UpdateAdopterDTO {


    @Schema(description = "Adopter's first name", example = "John")
    private String name;

    @Schema(description = "Adopter's last name", example = "Doe")
    private String lastname;

    @Schema(description = "Adopter's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Adopter's phone number", example = "+1234567890")
    private String phone;
}