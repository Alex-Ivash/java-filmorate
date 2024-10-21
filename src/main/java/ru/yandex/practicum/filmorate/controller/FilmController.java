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
        log.info("Поиск фильма c id={}...", id);
        Film film = filmService.find(id);
        log.info("Фильм с id={} найден.", id);

        return film;
    }

    @GetMapping
    private List<Film> getAll() {
        log.info("Формирование списка всех фильмов...");
        List<Film> allFilms = filmService.getAll();
        log.info("Список всех фильмов сформирован.");

        return allFilms;
    }

    @GetMapping("/popular")
    private List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Формирование топ {} популярных фильмов...", count);
        List<Film> popularFilms = filmService.getPopular(count);
        log.info("Список топ {} популярных фильмов сформирован.", count);

        return popularFilms;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Film create(@RequestBody @Valid Film film) {
        log.info("Создание нового фильма {}...", film);
        Film createdFilm = filmService.create(film);
        log.info("Создан новый фильм {}.", createdFilm);

        return createdFilm;
    }

    @PutMapping
    private Film update(@RequestBody @Validated({RestValidationGroups.Update.class, Default.class}) Film film) {
        log.info("Обновление фильма {}...", film);
        Film updatedFilm = filmService.update(film);
        log.info("Обновлен фильм {}.", updatedFilm);

        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    private void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Добавление лайка пользователя с id={} фильму с id={}...", userId, id);
        filmService.addLike(id, userId);
        log.info("Добавлен лайк пользователя с id={} фильму с id={}.", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Удаление лайка пользователя с id={} фильму с id={}...", userId, id);
        filmService.removeLike(id, userId);
        log.info("Удален лайк пользователя с id={} фильму с id={}.", userId, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void remove(@PathVariable long id) {
        log.info("Удаление фильма с id={}...", id);
        filmService.remove(id);
        log.info("Удален фильм c id={}.", id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void removeAll() {
        log.info("Удаление всех фильмов...");
        filmService.removeAll();
        log.info("Все фильмы удалены.");
    }
}
