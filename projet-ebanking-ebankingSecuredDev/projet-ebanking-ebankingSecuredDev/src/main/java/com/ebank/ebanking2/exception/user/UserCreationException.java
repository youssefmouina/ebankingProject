package com.ebank.ebanking2.exception.user;

import com.ebank.ebanking2.exception.AppException;

public class UserCreationException extends AppException {
    public UserCreationException(String message) {
        super(message);
    }
}
