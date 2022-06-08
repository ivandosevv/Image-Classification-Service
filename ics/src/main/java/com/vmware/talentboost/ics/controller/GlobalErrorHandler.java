package com.vmware.talentboost.ics.controller;

import com.vmware.talentboost.ics.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<ErrorDto> handleBadRequest(
        final RuntimeException ex, final WebRequest request) {
        final ErrorDto error = new ErrorDto();
        error.message = ex.getMessage();
        error.statusCode = HttpStatus.BAD_REQUEST.value();
        return new ResponseEntity<>(error, new HttpHeaders(),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { NoSuchElementException.class })
    protected ResponseEntity<ErrorDto> handleNotFound(final RuntimeException ex,
                                                      final WebRequest request) {
        final ErrorDto error = new ErrorDto();
        error.message = ex.getMessage();
        error.statusCode = HttpStatus.NOT_FOUND.value();
        return new ResponseEntity<>(error, new HttpHeaders(),
            HttpStatus.NOT_FOUND);
    }

}
