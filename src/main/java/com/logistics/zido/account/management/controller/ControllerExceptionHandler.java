package com.logistics.zido.account.management.controller;

import com.logistics.zido.account.management.exception.TokenNotFoundException;
import com.logistics.zido.account.management.exception.ZidoAccountManagerException;
import com.logistics.zido.account.management.helpers.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Error> httpErrorHandler(HttpClientErrorException e) {
        Error error;
        try {
            error = new Error("invalid_request", e.getMessage());
        } catch (Exception ex) {
            error = new Error("invalid_request", e.getMessage());
        }
        return new ResponseEntity<>(error, e.getStatusCode());
    }

    @ExceptionHandler(ZidoAccountManagerException.class)
    public ResponseEntity<Error> httpGoTaxExceptionHandler(ZidoAccountManagerException ge) {
        Error error;
        try {
            error = new Error("invalid_request", ge.getMessage());
        } catch (Exception ex) {
            error = new Error("invalid_request", ge.getMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Error> httpTokenNotFoundExceptionHandler(TokenNotFoundException ge) {
        Error error;
        try {
            error = new Error("invalid_request", ge.getMessage());
        } catch (Exception ex) {
            error = new Error("invalid_request", ge.getMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
