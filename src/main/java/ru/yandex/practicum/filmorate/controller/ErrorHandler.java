package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleHappinessOverflow(final NotFoundException e) {
        log.info(e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHappinessOverflow(final MethodArgumentNotValidException e) {
        log.info(e.getMessage());

        return new ErrorResponse(
                e.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(err -> ((FieldError) err).getField() + ":" + err.getDefaultMessage())
                        .toArray(String[]::new)
        );
    }
}
