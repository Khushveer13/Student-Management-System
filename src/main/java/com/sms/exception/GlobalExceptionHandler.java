package com.sms.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler to capture all application errors.
 * Separates handling logic for REST API endpoints vs server-rendered Thymeleaf Web pages.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles Bean Validation exceptions (HTTP 400 Bad Request) for API payloads.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation failed: {} for path {}", ex.getMessage(), request.getRequestURI());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles IllegalArgumentException (e.g. Duplicate Enrollment Numbers).
     * Routes response based on request URI (JSON error for REST, Alert message redirect for Web).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.warn("Business logic rule violated: {} for path {}", ex.getMessage(), request.getRequestURI());

        // Check if this is an API call
        if (request.getRequestURI().startsWith("/api")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(error);
        }

        // Otherwise redirect web view with alert flash message
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/students";
    }

    /**
     * Fallback exception handler to prevent leak of internal technical details.
     */
    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.error("Unexpected system exception occurred on path {}: ", request.getRequestURI(), ex);

        // Check if this is an API call
        if (request.getRequestURI().startsWith("/api")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

        // For web views, show a clean error message
        redirectAttributes.addFlashAttribute("errorMessage", "An unexpected system error occurred. Please try again.");
        return "redirect:/students";
    }
}
