package com.example.rescuehubproject.accounts.controllers;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.services.EmployeeService;
import com.example.rescuehubproject.accounts.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empl")
public class EmployeeController {

    private final EmployeeService employeeService; //EmployeeService is a class that contains methods for working with the database

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/authenticated")
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return employeeService.getUserInfo(userDetails);
    }

}