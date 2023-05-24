package com.example.rescuehubproject.accounts.controllers;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.pojo.ChangePass;
import com.example.rescuehubproject.accounts.responses.PasswordChanged;
import com.example.rescuehubproject.accounts.services.UserService;
import com.example.rescuehubproject.accounts.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.BAD_REQUEST) // when unauthorized
    public ResponseEntity<User> registerAccount(@Valid @RequestBody User user) {
        return userService.registerAccount(user);
    }

    @PostMapping("/changepass")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<PasswordChanged> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ChangePass changePass) {
        return userService.changePassword(userDetails, changePass);
    }

}