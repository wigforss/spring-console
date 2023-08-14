package org.kasource.spring.console.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalTagQueryException extends RuntimeException {

    public IllegalTagQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTagQueryException(String message) {
        super(message);
    }
}

