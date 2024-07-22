package ru.yandex.practicum.filmorate.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validation.annotation.NotBefore;

import java.time.LocalDate;

public class FilmReleaseDateValidator implements ConstraintValidator<NotBefore, LocalDate> {
    String value;
    @Override
    public void initialize(NotBefore constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return false;
        }

        LocalDate boundDate = LocalDate.parse(value);

        return date.isAfter(boundDate) || date.equals(boundDate);
    }
}
