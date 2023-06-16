package com.example.rescuehubproject.adopters.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Schema(name = "GetAdopterDTO", description = "DTO for getting adopter")
public class GetAdopterDTO {

    @Schema(description = "Adopter ID", example = "1")
    private Long id;

    @Schema(description = "Adopter email", example = "jan.kowalski@onet.pl")
    private String Email;

}
