package com.example.rescuehubproject.accounts.responses;

import com.example.rescuehubproject.accounts.entity.User;

public record DeleteUserResponse(
        String user,
        String status
) {
    public static DeleteUserResponse response(User user) {
        return new DeleteUserResponse(user.getEmail(), "Deleted successfully!");
    }
}
