package com.bfhl.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralised exception handler for the BFHL API.
 *
 * Catches:
 * <ul>
 *   <li>{@link MethodArgumentNotValidException} – bean-validation failures
 *       (e.g. missing "data" field) → 400 Bad Request</li>
 *   <li>{@link HttpMessageNotReadableException} – malformed JSON body → 400</li>
 *   <li>Any other {@link Exception} → 500 Internal Server Error</li>
 * </ul>
 *
 * Every error response sets {@code "is_success": false} so the caller can
 * always rely on that field to detect failure.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ── Validation errors (missing / null fields) ─────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        log.warn("Validation failed: {}", fieldErrors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(false, "Validation failed", fieldErrors));
    }

    // ── Malformed JSON ────────────────────────────────────────────────────────
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed request body: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(false, "Request body is missing or malformed", null));
    }

    // ── Catch-all ─────────────────────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(false, "An unexpected error occurred", null));
    }

    // ── Inner error-response record ───────────────────────────────────────────

    /**
     * Lightweight error payload returned for all failure scenarios.
     */
    public static class ErrorResponse {

        @JsonProperty("is_success")
        private final boolean isSuccess;

        @JsonProperty("message")
        private final String message;

        @JsonProperty("errors")
        private final Map<String, String> errors;

        public ErrorResponse(boolean isSuccess, String message, Map<String, String> errors) {
            this.isSuccess = isSuccess;
            this.message   = message;
            this.errors    = errors;
        }

        public boolean isSuccess()            { return isSuccess; }
        public String getMessage()            { return message; }
        public Map<String, String> getErrors(){ return errors; }
    }
}
