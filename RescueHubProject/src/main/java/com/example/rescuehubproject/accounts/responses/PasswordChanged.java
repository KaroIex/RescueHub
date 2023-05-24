package com.example.rescuehubproject.accounts.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "email",
        "status",
})
public class PasswordChanged {

    private String email;

    private String status = "The password has been updated successfully";

    PasswordChanged() {}

    public PasswordChanged(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }
}