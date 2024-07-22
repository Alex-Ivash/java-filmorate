package ru.yandex.practicum.filmorate.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validation.annotation.NoWhitespace;

public class StringNoWhitespaceValidator implements ConstraintValidator<NoWhitespace, String> {
    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null) {
            return false;
        }

        return !string.contains(" ");
    }
}
