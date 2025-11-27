package com.example.capstone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiError> handleMissingHeader(MissingRequestHeaderException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), 
                    "Missing required header: " + ex.getHeaderName() + ". Please include X-User-Id"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), 
                    "Invalid " + ex.getName() + " format: " + ex.getValue()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    "Internal server error"));
    }
}