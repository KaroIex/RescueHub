package com.example.rescuehubproject.adoption.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Setter
@Getter
@Schema(description = "Adoption Form Data Transfer Object")
public class AdoptionFormDTO {

    @Schema(description = "is the adopter's house big enough for the animal?", example = "true")
    private boolean hasBigGarden;

    @Schema(description = "is the adopter have other pets?", example = "true")
    private boolean hasOtherPets;

    @Schema(description = "is the adopter have much free time?", example = "true")
    private boolean hasMuchFreeTime;
}