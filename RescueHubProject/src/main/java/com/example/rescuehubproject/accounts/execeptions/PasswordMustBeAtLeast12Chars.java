package com.example.rescuehubproject.accounts.execeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Password length must be 12 chars minimum!")
public class PasswordMustBeAtLeast12Chars extends RuntimeException {
    public PasswordMustBeAtLeast12Chars() {
        super();
    }
}