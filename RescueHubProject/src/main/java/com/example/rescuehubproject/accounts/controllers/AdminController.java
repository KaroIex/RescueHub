package com.example.rescuehubproject.accounts.controllers;

import com.example.rescuehubproject.accounts.request.RoleRequest;
import com.example.rescuehubproject.accounts.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/api/admin/user/role") // admin can change user role
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleRequest request) {
        return adminService.updateRole(request);
    }

    @GetMapping("/api/admin/user") // admin can get all users
    public ResponseEntity<?> getAllUsers() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("/api/admin/user/{email}") // admin can delete user
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        return adminService.deleteUser(email);
    }

}
