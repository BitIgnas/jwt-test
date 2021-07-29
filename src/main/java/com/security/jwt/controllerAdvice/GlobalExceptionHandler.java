package com.security.jwt.controllerAdvice;

import com.security.jwt.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.NonUniqueResultException;
import java.sql.SQLDataException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NonUniqueResultException.class})
    public final ResponseEntity<ApiError> handleNonUniqueResultException(Exception ex, WebRequest request) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT.value())
                .timestamp(Instant.now())
                .message(ex.getMessage())
                .description("[User already is activated]")
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public final ResponseEntity<ApiError> handleSqlConstraintViolationException(Exception ex, WebRequest request) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }



}
