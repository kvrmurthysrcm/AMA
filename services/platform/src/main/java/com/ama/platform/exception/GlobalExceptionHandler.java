package com.ama.platform.exception;

import com.ama.common.api.ApiResponse;
import com.ama.common.exception.ApiException;
import com.ama.platform.util.TraceIdHolder;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleApiException(ApiException ex) {
        log.error("API exception traceId={} code={} message={}", TraceIdHolder.getTraceId(), ex.getCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.failure(ex.getMessage(), TraceIdHolder.getTraceId(), Map.of("code", ex.getCode())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toError)
                .toList();
        return ResponseEntity.badRequest()
                .body(ApiResponse.failure("Validation failed", TraceIdHolder.getTraceId(), Map.of("errors", errors)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleConstraint(ConstraintViolationException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.failure("Validation failed", TraceIdHolder.getTraceId(), Map.of("errors", ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception traceId={}", TraceIdHolder.getTraceId(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure("Unexpected error occurred", TraceIdHolder.getTraceId(), Map.of("code", "INTERNAL_SERVER_ERROR")));
    }

    private Map<String, String> toError(FieldError fieldError) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("field", fieldError.getField());
        map.put("message", fieldError.getDefaultMessage());
        return map;
    }
}
