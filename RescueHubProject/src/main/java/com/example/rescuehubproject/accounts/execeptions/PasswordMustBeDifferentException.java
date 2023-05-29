package com.example.rescuehubproject.accounts.execeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The passwords must be different!")
public class PasswordMustBeDifferentException extends RuntimeException {
    public PasswordMustBeDifferentException() {
        super();
    }
}