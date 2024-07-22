package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int seq = 0;
    private final LocalDate releaseDateLowerBound = LocalDate.of(1895, 12, 28);

    @PostMapping
    private Film createFilm(@RequestBody @Valid Film film) {
        film.setId(++seq);
        films.put(film.getId(), film);

        log.info("Добавлен новый фильм: " + film);

        return film;
    }

    @PutMapping
    private Film updateFilm(@RequestBody @Validated(RestValidationGroups.Update.class) Film film) {
        checkFilmExistence(film);
        films.put(film.getId(), film);

        log.info("Обновлен фильм: " + film);

        return film;
    }

    @GetMapping
    private Collection<Film> getFilms() {
        return films.values();
    }

    private void checkFilmExistence(Film film) {
        if (!films.containsKey(film.getId())) {
            String message = "Фильм с id=" + film.getId() + " не найден";

            log.warn(message);

            throw new NotFoundException(message);
        }
    }
}
