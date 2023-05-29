package com.example.rescuehubproject.accounts.execeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User exist!")  // 400 - Bad Request
public class UserExistException extends RuntimeException {
    public UserExistException() {
        super();
    }
}