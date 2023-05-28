package com.example.rescuehubproject.accounts.execeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User not found!")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super();
    }
}