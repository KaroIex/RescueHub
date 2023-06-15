package com.example.rescuehubproject.accounts.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ChangePassDTO", description = "DTO for change password")
public class ChangePass {

    @Schema(description = "New Password", example = "Password123@")
    private String new_password;

}