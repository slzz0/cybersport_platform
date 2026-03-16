package com.example.cybersport_platform.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleNotFound(
            NotFoundException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, "Resource not found", exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<ApiValidationError> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toValidationError)
                .toList();
        ApiError body = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                "Request contains invalid values",
                request.getRequestURI(),
                LocalDateTime.now(),
                validationErrors
        );
        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        List<ApiValidationError> validationErrors = exception.getConstraintViolations()
                .stream()
                .map(violation -> new ApiValidationError(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();
        ApiError body = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                "Request contains invalid values",
                request.getRequestURI(),
                LocalDateTime.now(),
                validationErrors
        );
        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public org.springframework.http.ResponseEntity<ApiError> handleBadRequest(Exception exception, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "Bad request", exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ApiError> handleUnexpected(Exception exception, HttpServletRequest request) {
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    private org.springframework.http.ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String error,
            String message,
            String path
    ) {
        ApiError body = new ApiError(
                status.value(),
                error,
                message,
                path,
                LocalDateTime.now(),
                List.of()
        );
        return org.springframework.http.ResponseEntity.status(status).body(body);
    }

    private ApiValidationError toValidationError(FieldError fieldError) {
        String message = fieldError.getDefaultMessage() == null ? "Invalid value" : fieldError.getDefaultMessage();
        return new ApiValidationError(fieldError.getField(), message);
    }
}
