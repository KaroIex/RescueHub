package com.example.rescuehubproject.adopters.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Schema(name = "CreateAdopterDTO", description = "DTO representing a new adopter")
public class CreateAdopterDTO {
    @Schema(description = "Adopter phone", example = "123456789")
    @NotEmpty(message = "phone required")
    private String phone;

    @Schema(description = "Adopter street", example = "ul. Kolorowa 12")
    @NotEmpty(message = "street required")
    private String street;


    @Schema(description = "Adopter city", example = "Wrocław")
    @NotEmpty(message = "city required")
    private String city;

    @Schema(description = "Adopter state", example = "Dolnośląskie")
    @NotEmpty(message = "state required")
    private String state;

    @Schema(description = "Adopter zip", example = "50-001")
    @NotEmpty(message = "zip required")
    private String zip;

    @NotEmpty(message = "country required")
    @Schema(description = "Adopter country", example = "Polska")
    private String country;

}