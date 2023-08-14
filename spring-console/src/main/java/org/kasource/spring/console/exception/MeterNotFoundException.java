package org.kasource.spring.console.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MeterNotFoundException extends RuntimeException {

    public MeterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeterNotFoundException(String message) {
        super(message);
    }
}

