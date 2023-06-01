package com.example.rescuehubproject.accounts.responses;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.util.Role;
import java.util.List;

public record SignUpResponse(
        Long id,
        String name,
        String lastname,
        String email,
        List<Role> roles) {

    public static SignUpResponse response(User user) {
        return new SignUpResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                user.getRoles());
    }
}
