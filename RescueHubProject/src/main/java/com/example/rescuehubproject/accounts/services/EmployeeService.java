package com.example.rescuehubproject.accounts.services;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final UserRepository userRepository;

    public EmployeeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> getUserInfo(UserDetailsImpl userDetails) { // get user info by email
        Optional<User> user = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        return ResponseEntity.of(user); // return user info
    }

}