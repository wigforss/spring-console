package org.kasource.spring.console.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NoUniqueMeterException extends RuntimeException {

    public NoUniqueMeterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoUniqueMeterException(String message) {
        super(message);
    }
}

