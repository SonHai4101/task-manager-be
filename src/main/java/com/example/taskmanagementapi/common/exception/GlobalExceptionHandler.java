package com.example.taskmanagementapi.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.taskmanagementapi.dto.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiError> handlerIllegalArgument(IllegalArgumentException ex) {
                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(new ApiError(409, "CONFLICT", ex.getMessage()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidation(
                        MethodArgumentNotValidException ex) {
                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .findFirst()
                                .map(err -> err.getField() + " " + err.getDefaultMessage())
                                .orElse("Validation failed");

                return ResponseEntity
                                .badRequest()
                                .body(new ApiError(400, "VALIDATION_ERROR", message));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiError> handleAccessDenied(
                        AccessDeniedException ex) {
                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(new ApiError(403, "FORBIDDEN",
                                                "You do not have permission to perform this action"));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGeneric(Exception ex) {
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiError(500, "INTERNAL_SERVER_ERROR", "Something went wrong"));
        }
}
