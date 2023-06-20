package com.example.rescuehubproject.adopters.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = org.springframework.http.HttpStatus.BAD_REQUEST, reason = "Empty adopter details!")
public class EmptyAdopterDetailsExceptions extends RuntimeException {
    public EmptyAdopterDetailsExceptions() {
        super();
    }
}
