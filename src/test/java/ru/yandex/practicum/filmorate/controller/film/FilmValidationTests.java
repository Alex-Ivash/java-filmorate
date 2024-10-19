package ru.yandex.practicum.filmorate.controller.film;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTests {
    private static Validator validator;

    static Film getValidFilm() {
        return new Film(1L, null, "name", "description", LocalDate.of(2005, 11, 1), 123, null);
    }

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Валидация проходит, если все поля фильма корректны")
    void passValidation_validFilm() {
        Film film = getValidFilm();

        var violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Название фильма не может быть пустым")
    void name_failValidation_IsBlank() {
        Film film = getValidFilm();
        film.setName(" ");

        var violations = validator.validate(film);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("name", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Название фильма не может быть null")
    void name_failValidation_IsNull() {
        Film film = getValidFilm();
        film.setName(null);

        var violations = validator.validate(film);

        assertAll(
                () -> assertEquals(2, violations.size()),
                () -> assertEquals("name", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Длина описания не может быть больше 200 символов")
    void description_failValidation_lengthMore200() {
        Film film = getValidFilm();
        film.setDescription("f".repeat(201));

        var violations = validator.validate(film);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("description", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Длина описания может быть ровно 200 символов")
    void description_passValidation_lengthEquals200() {
        Film film = getValidFilm();
        film.setDescription("f".repeat(200));

        var violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Описание может быть пустым")
    void description_passValidation_lengthLess200() {
        Film film = getValidFilm();
        film.setDescription("");

        var violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Описание не может быть null")
    void description_passValidation_isNull() {
        Film film = getValidFilm();
        film.setDescription(null);

        var violations = validator.validate(film);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("description", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Дата релиза не может быть раньше 28 декабря 1895 года")
    void releaseDate_failValidation_before28_12_1895() {
        Film film = getValidFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        var violations = validator.validate(film);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("releaseDate", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Дата релиза не может быть null")
    void releaseDate_failValidation_isNull() {
        Film film = getValidFilm();
        film.setReleaseDate(null);

        var violations = validator.validate(film);

        assertAll(
                () -> assertEquals(2, violations.size()),
                () -> assertEquals("releaseDate", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Дата релиза может быть ровно 28 декабря 1895 года")
    void releaseDate_failValidation_equals28_12_1895() {
        Film film = getValidFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        var violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Дата релиза может быть позже 28 декабря 1895 года")
    void releaseDate_passValidation_after28_12_1895() {
        Film film = getValidFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 29));

        var violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Продолжительность не может быть отрицательной")
    void duration_failValidation_isNegative() {
        Film film = getValidFilm();
        film.setDuration(-100);

        var violations = validator.validate(film);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("duration", violations.iterator().next().getPropertyPath().toString())
        );
    }

    @Test
    @DisplayName("Продолжительность не может быть null")
    void duration_failValidation_isNull() {
        Film film = getValidFilm();
        film.setDuration(null);

        var violations = validator.validate(film);

        assertAll(
                () -> assertEquals(1, violations.size()),
                () -> assertEquals("duration", violations.iterator().next().getPropertyPath().toString())
        );
    }
}
