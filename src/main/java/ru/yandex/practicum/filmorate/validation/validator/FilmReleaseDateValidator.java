package ru.yandex.practicum.filmorate.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validation.annotation.DateNotBefore;

import java.time.LocalDate;

public class FilmReleaseDateValidator implements ConstraintValidator<DateNotBefore, LocalDate> {
    LocalDate releaseDateLowerBound;

    @Override
    public void initialize(DateNotBefore constraintAnnotation) {
        this.releaseDateLowerBound = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return false;
        }

        return date.isAfter(releaseDateLowerBound) || date.equals(releaseDateLowerBound);
    }
}
