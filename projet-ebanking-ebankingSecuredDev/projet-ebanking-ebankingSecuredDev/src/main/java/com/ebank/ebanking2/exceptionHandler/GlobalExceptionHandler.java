package com.ebank.ebanking2.exceptionHandler;

import com.ebank.ebanking2.exception.PathVarException;
import com.ebank.ebanking2.exception.token.TokenCreationException;
import com.ebank.ebanking2.exception.token.TokenDeleteException;
import com.ebank.ebanking2.exception.token.TokenGetException;
import com.ebank.ebanking2.exception.token.TokenUpdateException;
import com.ebank.ebanking2.exception.user.UserGetException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(2)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserGetException.class)
    public ResponseEntity<?> handleUserGetException(UserGetException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse("USER_FETCH_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(TokenCreationException.class)
    public ResponseEntity<?> handleTokenCreationException(TokenCreationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("TOKEN_CREATION_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(TokenUpdateException.class)
    public ResponseEntity<?> handleTokenUpdateException(TokenUpdateException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("TOKEN_UPDATE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(TokenDeleteException.class)
    public ResponseEntity<?> handleTokenDeleteException(TokenDeleteException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("TOKEN_DELETE_FAILED", ex.getMessage()));
    }
    @ExceptionHandler(TokenGetException.class)
    public ResponseEntity<?> handleTokenGetException(TokenGetException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse("TOKEN_FETCH_FAILED", ex.getMessage()));
    }



    @ExceptionHandler(PathVarException.class)
    public ResponseEntity<?> handlePathVarException(PathVarException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse("PATH_VAR_ERROR", ex.getMessage()));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("ACCESS_DENIED", "You do not have permission to perform this operation.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("GENERAL_ERROR", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}

