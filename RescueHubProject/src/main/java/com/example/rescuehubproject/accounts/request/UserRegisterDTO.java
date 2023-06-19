package com.example.rescuehubproject.accounts.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "UserRegisterDTO", description = "Data Transfer Object for User Registration")
@Setter
@Getter
public class UserRegisterDTO {
    @Schema(name = "name", description = "User's name", example = "John")
    private String name;
    @Schema(name = "lastname", description = "User's lastname", example = "Doe")
    private String lastname;

    @Schema(name = "email", description = "User's email", example = "example@gmail")
    private String email;

    @Schema(name = "password", description = "User's password", example = "password!QAZ")
    private String password;

}
