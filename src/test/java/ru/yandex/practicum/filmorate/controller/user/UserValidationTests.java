package ru.yandex.practicum.filmorate.controller.user;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTests {
    private static Validator validator;

    static User getValidUser() {
        return new User(1L, "test@mail.com", "login", "name", LocalDate.of(1998, 4, 23));
    }

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Валидация проходит, если все поля пользователя корректны")
    void passValidation_validUser() {
        User user = getValidUser();

        var violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Электронная почта должна соответствовать формату")
    void email_failValidation_IsEmpty() {
        User user = getValidUser();
        user.setEmail("@foo.bar");

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("email", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Электронная почта не может быть пустой")
    void email_failValidation_IsBlank() {
        User user = getValidUser();
        user.setEmail(" ");

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(2, violations.size()),
                () -> assertEquals("email", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Электронная почта не может быть null")
    void email_failValidation_IsNull() {
        User user = getValidUser();
        user.setEmail(null);

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(2, violations.size()),
                () -> assertEquals("email", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Логин не может быть null")
    void login_failValidation_IsNull() {
        User user = getValidUser();
        user.setLogin(null);

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(3, violations.size()),
                () -> assertEquals("login", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Логин не может быть пустым")
    void login_failValidation_isBlank() {
        User user = getValidUser();
        user.setLogin("  ");

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(2, violations.size()),
                () -> assertEquals("login", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Логин не может содержить пробелы в начале")
    void login_failValidation_WhitespacesBeginning() {
        User user = getValidUser();
        user.setLogin("\n foo");

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("login", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Логин не может содержить пробелы в середине")
    void login_failValidation_WhitespacesMiddle() {
        User user = getValidUser();
        user.setLogin("foo \t bar");

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("login", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Логин не может содержить пробелы в конце")
    void login_failValidation_WhitespacesEnd() {
        User user = getValidUser();
        user.setLogin("bar  \t\n");

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("login", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("День рождения не может быть в будущем")
    void birthday_failValidation_Future() {
        User user = getValidUser();
        user.setBirthday(LocalDate.now().plusDays(1));

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("birthday", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("День рождения не может быть null")
    void birthday_failValidation_isNull() {
        User user = getValidUser();
        user.setBirthday(null);

        var violations = validator.validate(user);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("birthday", violations.iterator().next().getPropertyPath().toString())
        );
    }
}
