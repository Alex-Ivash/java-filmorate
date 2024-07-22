package ru.yandex.practicum.filmorate.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.validation.validator.StringNoWhitespaceValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StringNoWhitespaceValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoWhitespace {
    String message() default "Строка не должна содержать пробельные символы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
