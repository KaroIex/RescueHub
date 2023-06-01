package com.example.rescuehubproject.accounts.responses;

import com.example.rescuehubproject.accounts.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record UserListResponse(
        Long id,
        String name,
        String lastname,
        String email,
        List<String> roles
) {
    public static UserListResponse response(User user) {
        List<String> roles = new ArrayList<>();
        for (var role : user.getRoles()) {
            roles.add(role.name());
        }

        Collections.sort(roles);
        return new UserListResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                roles
        );
    }
}
