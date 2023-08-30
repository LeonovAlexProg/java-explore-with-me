package com.leonovalexprog.exception;

import com.leonovalexprog.exception.exceptions.IncorrectPasswordException;
import com.leonovalexprog.exception.exceptions.UserExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestHandler(final IncorrectPasswordException e) {
        log.warn(e.getMessage());

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                "Incorrect password",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userExistsHandler(final UserExistsException e) {
        log.warn(e.getMessage());

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                "Credentials error",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}