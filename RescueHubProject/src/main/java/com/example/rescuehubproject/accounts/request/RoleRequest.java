package com.example.rescuehubproject.accounts.request;

import com.example.rescuehubproject.accounts.util.RoleOperation;

public record RoleRequest(
        String user,
        String role,
        RoleOperation operation
) {
}
