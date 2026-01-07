package com.example.taskmanagementapi.common.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.taskmanagementapi.dto.ApiError;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
        private final MessageSource messageSource;

        private String msg(String key) {
                return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        }

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
                                .map(err -> err.getField() + " "
                                                + messageSource.getMessage(err.getDefaultMessage(), null,
                                                                LocaleContextHolder.getLocale()))
                                .orElse(msg("error.validation"));

                return ResponseEntity
                                .badRequest()
                                .body(new ApiError(400, "VALIDATION_ERROR", message));
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ApiError> handleAuth(
                        AuthenticationException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new ApiError(401, "UNAUTHORIZED", msg("error.unauthorized")));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiError> handleAccessDenied(
                        AccessDeniedException ex) {
                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(new ApiError(403, "FORBIDDEN",
                                                msg("error.forbidden")));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGeneric(Exception ex) {
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiError(500, "INTERNAL_SERVER_ERROR", msg("error.internal")));
        }
}
