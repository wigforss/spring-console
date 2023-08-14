package org.kasource.spring.console.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalTagOperatorException extends RuntimeException {

    public IllegalTagOperatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTagOperatorException(String message) {
        super(message);
    }
}

