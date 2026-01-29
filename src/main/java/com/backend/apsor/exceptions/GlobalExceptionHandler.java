package com.backend.apsor.exceptions;

import com.backend.apsor.payloads.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String TRACE_ID = "traceId";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleBodyValidation(MethodArgumentNotValidException ex,
                                                                 HttpServletRequest req) {
        List<ApiErrorResponse.FieldViolation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toViolation)
                .toList();

        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed", req, violations, false, ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleParamValidation(ConstraintViolationException ex,
                                                                  HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed", req, null, false, ex);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex, HttpServletRequest req) {
        // Business exceptions are expected: log as warn, not error
        log.warn("Handled ApiException code={} status={} path={} msg={}",
                ex.getCode(), ex.getStatus().value(), req.getRequestURI(), ex.getMessage());

        return build(ex.getStatus(), ex.getCode(), ex.getMessage(), req, null, false, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuth(AuthenticationException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication required", req, null, false, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "FORBIDDEN", "Access denied", req, null, false, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                                  HttpServletRequest req) {
        // Treat as client error
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), req, null, false, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAny(Exception ex, HttpServletRequest req) {
        // Unexpected errors: log full stacktrace, return generic message
        log.error("Unhandled error path={} msg={}", req.getRequestURI(), ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "Something went wrong", req, null, true, null);
    }

    private ApiErrorResponse.FieldViolation toViolation(FieldError fe) {
        return ApiErrorResponse.FieldViolation.builder()
                .field(fe.getField())
                .message(fe.getDefaultMessage())
                .build();
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status,
                                                   String code,
                                                   String message,
                                                   HttpServletRequest req,
                                                   List<ApiErrorResponse.FieldViolation> violations,
                                                   boolean hideDetails,
                                                   Exception exForDebug) {
        String traceId = MDC.get(TRACE_ID);
        String finalMessage = hideDetails ? "Something went wrong" : message;

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .code(code)
                .message(finalMessage)
                .path(req.getRequestURI())
                .traceId(traceId)
                .violations(violations == null || violations.isEmpty() ? null : violations)
                .build();

        if (exForDebug != null && status.is4xxClientError()) {
            log.debug("Client error code={} status={} path={}", code, status.value(), req.getRequestURI(), exForDebug);
        }

        return ResponseEntity.status(status).body(body);
    }
}
