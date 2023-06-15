package com.example.rescuehubproject.adopters.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is not an adopter!")
public class UserIsNotAnAdopter extends RuntimeException {
    public UserIsNotAnAdopter() {
        super();
    }
}
