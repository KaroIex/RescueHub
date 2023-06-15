package com.example.rescuehubproject.accounts.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AuthenticationDTO", description = "DTO for authentication")
public class AuthenticationRequest {
    @Schema(description = "User email", example = "test@test.com")
    private String email;
    @Schema(description = "User password", example = "test123456")
    private String password;
}