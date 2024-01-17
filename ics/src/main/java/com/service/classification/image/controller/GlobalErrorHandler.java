package com.service.classification.image.controller;

import java.util.NoSuchElementException;

import com.service.classification.image.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalErrorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<ErrorDto> handleBadRequest(
        final RuntimeException e, final WebRequest request) {
		LOGGER.error("An exception of type IllegalArgumentException has occurred: {}", e.getMessage(), e);

        final ErrorDto error = new ErrorDto();
        error.message = e.getMessage();
        error.statusCode = HttpStatus.BAD_REQUEST.value();
        return new ResponseEntity<>(error, new HttpHeaders(),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { NoSuchElementException.class })
    protected ResponseEntity<ErrorDto> handleNotFound(final RuntimeException e,
                                                      final WebRequest request) {
		LOGGER.error("An exception of type NoSuchElementException has occurred: {}", e.getMessage(), e);
        final ErrorDto error = new ErrorDto();
        error.message = e.getMessage();
        error.statusCode = HttpStatus.NOT_FOUND.value();
        return new ResponseEntity<>(error, new HttpHeaders(),
            HttpStatus.NOT_FOUND);
    }

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		LOGGER.error("An error occurred: {}", e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	}

}
