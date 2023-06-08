package com.example.rescuehubproject.accounts.util;


import com.example.rescuehubproject.accounts.execeptions.RoleNotFoundException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public enum Role {
    USER,
    ADOPTER,
    ADMINISTRATOR,

    ROLE_USER,
    ROLE_ADOPTER,
    ROLE_ADMINISTRATOR;

    public static Role getRole(String role) {
        return switch (role) {
            case "USER" -> ROLE_USER;
            case "ADOPTER" -> ROLE_ADOPTER;
            case "ADMINISTRATOR" -> ROLE_ADMINISTRATOR;
            default -> throw new RoleNotFoundException("Role not found!");
        };
    }
}
