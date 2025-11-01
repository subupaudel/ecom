package com.ecommerce.ecommerce.exception;

import com.ecommerce.ecommerce.dto.Response;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Catch all unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAllException(Exception ex, WebRequest request) {
        Response errorResponse = Response.builder()
                .status(HttpStatus.BAD_REQUEST.value()) // Use 400 instead of 500
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Resource not found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException ex, WebRequest request) {
        Response errorResponse = Response.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Invalid credentials
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Response> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest request) {
        Response errorResponse = Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // JWT issues (malformed, expired, invalid)
//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity<Response> handleJwtException(JwtException ex, WebRequest request) {
//        Response errorResponse = Response.builder()
//                .status(HttpStatus.BAD_REQUEST.value())
//                .message("Invalid or malformed JWT token")
//                .build();
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }

    // Duplicate email/phone (database constraint violation)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        Response errorResponse = Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Duplicate entry for email or phone number")
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
