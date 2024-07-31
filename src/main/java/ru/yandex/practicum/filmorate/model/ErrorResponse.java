package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String[] errors;

    public ErrorResponse(String... errorMessages) {
        this.errors = errorMessages;
    }
}
