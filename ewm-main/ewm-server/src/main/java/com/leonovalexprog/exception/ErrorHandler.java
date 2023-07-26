package com.leonovalexprog.exception;

import com.leonovalexprog.exception.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final String datetimePattern = "yyyy-MM-dd HH:mm:ss";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse integrityConstraintViolation(final FieldValueExistsException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(
              HttpStatus.CONFLICT.name(),
              "Integrity constraint has been violated",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(datetimePattern))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidHandler(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                "Incorrectly made request.",
                String.format("Field: %s. Error: %s. Value: %s",
                        e.getBindingResult().getFieldError().getField(),
                        e.getBindingResult().getFieldError().getDefaultMessage(),
                        e.getBindingResult().getFieldError().getRejectedValue()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(datetimePattern))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestHandler(final BadRequestException e) {
        log.warn(e.getMessage());

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(datetimePattern))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotExistsHandler(final EntityNotExistsException e) {
        log.warn(e.getMessage());

        return new ErrorResponse(
                HttpStatus.NOT_FOUND.name(),
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(datetimePattern))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conditionsViolationHandler(final ConditionsViolationException e) {
        log.warn(e.getMessage());

        return new ErrorResponse(
                HttpStatus.CONFLICT.name(),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(datetimePattern))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse missingServletRequestParameterHandler(final MissingServletRequestParameterException e) {
        log.warn(e.getMessage());

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                "Missing request parameter",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(datetimePattern))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse dataValidationFailHandler(final DataValidationFailException e) {
        log.warn(e.getMessage());

        return new ErrorResponse(
                HttpStatus.FORBIDDEN.name(),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(datetimePattern))
        );
    }
}
