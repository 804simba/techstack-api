package com.techstack.api.core.exception;

import com.techstack.api.payload.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException e) {
        log.error("AppException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().success(false).code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()).timestamp(LocalDateTime.now()).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAppException(Exception e) {
        log.error("Exception: {}", e.getMessage());
        log.error("Stack trace: {}", Arrays.toString(e.getStackTrace()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().success(false).code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()).timestamp(LocalDateTime.now()).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("MethodArgumentNotValidException: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().success(false).code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage).timestamp(LocalDateTime.now()).build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        ErrorResponse errorResponse;
        if (e instanceof ConstraintViolationException) {
            String violations = extractViolationsFromException((ConstraintViolationException) e);
            log.error("ConstraintViolationException: {}", violations);
            errorResponse = ErrorResponse.builder().success(false).code(HttpStatus.BAD_REQUEST.value())
                    .message(violations).timestamp(LocalDateTime.now()).build();
        } else {
            String exceptionMessage = e.getMessage();
            log.error("ValidationException: {}", exceptionMessage);
            errorResponse = ErrorResponse.builder().success(false).code(HttpStatus.BAD_REQUEST.value())
                    .message(exceptionMessage).timestamp(LocalDateTime.now()).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private String extractViolationsFromException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().success(false).code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()).timestamp(LocalDateTime.now()).build());
    }
}
