package com.ebank.ebanking2.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserGetException extends ResponseStatusException {
    public UserGetException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
