package com.example.rescuehubproject.adopters.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Animal not found!")
public class AnimalNotFoundException extends RuntimeException {
    public AnimalNotFoundException() {
        super();
    }
}
