package com.example.rescuehubproject.accounts.responses;

import com.example.rescuehubproject.accounts.entity.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.SortNatural;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonPropertyOrder({
        "id",
        "name",
        "lastname",
        "email",
        "roles"
})
public record RoleResponse(
        Long id,
        String name,
        String lastname,
        String email,
        @SortNatural
        List<String> roles
) {
    public static RoleResponse response(User user) {
        List<String> roles = new ArrayList<>();
        for (var role : user.getRoles()) {
            roles.add(role.name());
        }
        Collections.sort(roles);
        return new RoleResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                roles
        );
    }
}
