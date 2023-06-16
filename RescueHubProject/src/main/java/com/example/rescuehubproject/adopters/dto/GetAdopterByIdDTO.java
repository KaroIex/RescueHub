package com.example.rescuehubproject.adopters.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
@Schema(name = "GetAdopterByIdDTO", description = "DTO for getting adopter by ID")
public class GetAdopterByIdDTO {

    @Schema(description = "Adopter ID", example = "1")
    private Long id;

    @Schema(description = "Adopter name", example = "Jan")
    private String name;

    @Schema(description = "Adopter lastname", example = "Kowalski")
    private String lastname;

    @Schema(description = "Adopter email", example = "user.example@test.com")
    private String email;

    @Schema(description = "Adopter phone", example = "123456789")
    private String phone;
}
