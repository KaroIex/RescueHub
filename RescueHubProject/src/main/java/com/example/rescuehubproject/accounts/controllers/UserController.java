package com.example.rescuehubproject.accounts.controllers;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.request.AuthenticationRequest;
import com.example.rescuehubproject.accounts.request.ChangePass;
import com.example.rescuehubproject.accounts.responses.AuthenticationResponse;
import com.example.rescuehubproject.accounts.responses.PasswordChanged;
import com.example.rescuehubproject.security.JwtUtils;
import com.example.rescuehubproject.security.UserDetailsImpl;
import com.example.rescuehubproject.accounts.services.UserService;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST) // when unauthorized
    public ResponseEntity<User> registerAccount(@Valid @RequestBody User user) {
        return userService.registerAccount(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/changepass")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<PasswordChanged> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ChangePass changePass) {
        return userService.changePassword(userDetails, changePass);
    }



}