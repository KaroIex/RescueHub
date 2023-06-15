package com.example.rescuehubproject.accounts.request;

import com.example.rescuehubproject.accounts.util.RoleOperation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RoleRequestDTO", description = "DTO for role request")
public record RoleRequest(
        @Schema(description = "User email", example = "test123@test.com")
        String user,
        @Schema(description = "Role name", example = "ADOPTER")
        String role,
        @Schema(description = "Role operation", example = "GRANT/REMOVE")
        RoleOperation operation
) {
}
