package com.example.rescuehubproject.accounts.controllers;

import com.example.rescuehubproject.accounts.request.AuthenticationRequest;
import com.example.rescuehubproject.accounts.request.ChangePass;
import com.example.rescuehubproject.accounts.request.UserRegisterDTO;
import com.example.rescuehubproject.accounts.responses.AuthenticationResponse;
import com.example.rescuehubproject.accounts.responses.PasswordChanged;
import com.example.rescuehubproject.accounts.services.UserService;
import com.example.rescuehubproject.security.JwtUtils;
import com.example.rescuehubproject.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
@Tag(name = "Users", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User created")
    @ApiResponse(responseCode = "400", description = "User account not created")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<UserRegisterDTO> registerAccount(@Valid @RequestBody @Parameter(description = "Details to create user") UserRegisterDTO user) {
        return ResponseEntity.ok(userService.registerAccount(user));
    }

    @PostMapping("/signin")
    @Operation(summary = "Endpoint for authentication")
    @ApiResponse(responseCode = "200", description = "User authenticated")
    @ApiResponse(responseCode = "400", description = "User not authenticated")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody @Parameter(description = "Details to authenticate") AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/changepass")
    @Operation(summary = "Endpoint for changing password")
    @ApiResponse(responseCode = "200", description = "Password changed")
    @ApiResponse(responseCode = "400", description = "Password not changed")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<PasswordChanged> changePassword(@AuthenticationPrincipal @Parameter(description = "Current user details") UserDetailsImpl userDetails, @Valid @RequestBody @Parameter(description = "New password") ChangePass changePass) {
        return userService.changePassword(userDetails, changePass);
    }


}