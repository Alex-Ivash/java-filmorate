package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

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
        if (film.getReleaseDate().isBefore(releaseDateLowerBound)) {
            ResponseStatusException responseStatusException = new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Дата релиза — не раньше 28 декабря 1895 года"
            );

            log.warn(responseStatusException.getMessage());
            throw responseStatusException;
        }

        film.setId(++seq);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: " + film);

        return film;
    }

    @PutMapping
    private Film updateFilm(@RequestBody @Valid Film film) {
        if (film.getId() == null) {
            ResponseStatusException responseStatusException = new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Id должен быть указан"
            );

            log.warn(responseStatusException.getMessage());
            throw responseStatusException;
        }

        if (!films.containsKey(film.getId())) {
            ResponseStatusException responseStatusException = new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Фильм с id = " + film.getId() + " не найден"
            );

            log.warn(responseStatusException.getMessage());
            throw responseStatusException;
        }

        films.put(film.getId(), film);
        log.info("Обновлен фильм: " + film);

        return film;
    }

    @GetMapping
    private Collection<Film> getFilms() {
        return films.values();
    }
}
