package com.devmeetups.app.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiError> handleBeanValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    var violations = ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> Map.of(
            "field", fe.getField(),
            "message", fe.getDefaultMessage()))
        .toList();

    return build(HttpStatus.BAD_REQUEST, "validation", "Validation failed", req.getRequestURI(),
        Map.of("violations", violations));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
    var violations = ex.getConstraintViolations().stream()
        .map(cv -> Map.of("field", cv.getPropertyPath().toString(), "message", cv.getMessage()))
        .toList();

    return build(HttpStatus.BAD_REQUEST, "validation", "Validation failed", req.getRequestURI(),
        Map.of("violations", violations));
  }

  @ExceptionHandler(NoSuchElementException.class)
  ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex, HttpServletRequest req) {
    return build(HttpStatus.NOT_FOUND, "not-found", ex.getMessage(), req.getRequestURI(), Map.of());
  }

  @ExceptionHandler(IllegalStateException.class)
  ResponseEntity<ApiError> handleConflict(IllegalStateException ex, HttpServletRequest req) {
    return build(HttpStatus.CONFLICT, "conflict", ex.getMessage(), req.getRequestURI(), Map.of());
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "server-error", "Unexpected error", req.getRequestURI(), Map.of());
  }

  private ResponseEntity<ApiError> build(HttpStatus status, String type, String title, String path,
      Map<String, Object> extras) {
    var body = new ApiError(
        type, title, status.value(), null, path, Instant.now(),
        extras == null ? Map.of() : extras);
    return ResponseEntity.status(status)
        .contentType(MediaType.APPLICATION_JSON)
        .body(body);
  }
}
