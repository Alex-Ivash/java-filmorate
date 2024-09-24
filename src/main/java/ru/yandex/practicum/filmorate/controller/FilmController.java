package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    private Film get(@PathVariable long id) {
        return filmService.find(id);
    }

    @GetMapping
    private List<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/popular")
    private List<Film> getPoular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Film create(@RequestBody @Valid Film film) {
        log.info("Попытка создать новый фильм {}", film);
        Film createdFilm = filmService.create(film);
        log.info("Создан новый фильм {}", createdFilm);

        return createdFilm;
    }

    @PutMapping
    private Film update(@RequestBody @Validated({RestValidationGroups.Update.class, Default.class}) Film film) {
        log.info("Попытка обновить фильм {}", film);

        Film updatedFilm = filmService.update(film);
        log.info("Обновлен фильм {}", updatedFilm);

        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    private void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Попытка пользователя с id={} поставить лайк фильму c id={}", userId, id);

        filmService.addLike(id, userId);
        log.info("Пользователь c id={} поставил лайк фильму c id={}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Попытка пользователя с id={} удалить свой лайк фильму c id={}", userId, id);

        filmService.removeLike(id, userId);
        log.info("Пользователь id={} удалил свой лайк фильму id={}", userId, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void remove(@PathVariable long id) {
        log.info("Попытка удалить фильм с id={}", id);

        filmService.remove(id);
        log.info("Удален фильм c id={}", id);
    }
}
