package com.example.rescuehubproject.accounts.controllers;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.security.UserDetailsImpl;
import com.example.rescuehubproject.accounts.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empl")
@Tag(name = "Employee", description = "Endpoints for employee management")
public class EmployeeController {

    private final EmployeeService employeeService; //EmployeeService is a class that contains methods for working with the database

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/authenticated")
    @Operation(summary = "Endpoint for authentication")
    @ApiResponse(responseCode = "200", description = "User authenticated")
    @ApiResponse(responseCode = "400",  description = "User not authenticated")
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal @Parameter(description = "Current user details") UserDetailsImpl userDetails) {
        return employeeService.getUserInfo(userDetails);
    }

    @GetMapping("/protected")
    @Operation(summary = "Endpoint for authentication")
    @ApiResponse(responseCode = "200", description = "User authenticated")
    @ApiResponse(responseCode = "400",  description = "User not authenticated")
    public ResponseEntity<String> getProtectedMessage() {
        return ResponseEntity.ok("To jest chroniona wiadomość dostępna tylko dla zalogowanych użytkowników.");
    }
}

