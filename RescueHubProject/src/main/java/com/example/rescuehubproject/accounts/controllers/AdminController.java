package com.example.rescuehubproject.accounts.controllers;

import com.example.rescuehubproject.accounts.request.RoleRequest;
import com.example.rescuehubproject.accounts.services.AdminService;
import com.example.rescuehubproject.accounts.services.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Admin", description = "Endpoints for admin management")
public class AdminController {

    private final AdminService adminService;
    private final LogService logService;

    public AdminController(AdminService adminService, LogService logService) {
        this.adminService = adminService;
        this.logService = logService;
    }

    @PutMapping("/api/admin/user/role") // admin can change user role
    @Operation(summary = "Endpoint for changing user role")
    @ApiResponse(responseCode = "200", description = "User role changed")
    @ApiResponse(responseCode = "400", description = "User role not changed")
    public ResponseEntity<?> updateRole(@Valid @RequestBody @Parameter(description = "Details to change role") RoleRequest request) {
        return adminService.updateRole(request);
    }

    @GetMapping("/api/admin/user") // admin can get all users
    @Operation(summary = "Endpoint for getting all users from db")
    @ApiResponse(responseCode = "200", description = "Users retrieved")
    @ApiResponse(responseCode = "400", description = "Users not retrieved")
    public ResponseEntity<?> getAllUsers() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("/api/admin/user/{email}") // admin can delete user
    @Operation(summary = "Endpoint for deleting user from db")
    @ApiResponse(responseCode = "200", description = "User deleted")
    @ApiResponse(responseCode = "400", description = "User not deleted")
    public ResponseEntity<?> deleteUser(@PathVariable @Parameter(description = "User email") String email) {
        return adminService.deleteUser(email);
    }

    @GetMapping("/api/admin/logs") // admin can get all logs from db
    @Operation(summary = "Endpoint for retrieving all saved logs from db")
    @ApiResponse(responseCode = "200", description = "Logs retrieved")
    @ApiResponse(responseCode = "400", description = "Logs not retrieved")
    public ResponseEntity<?> getLogs() {
        return logService.getAllLogs();
    }

}
