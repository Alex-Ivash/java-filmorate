package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    private List<Genre> getAll() {
        log.info("Формирование списка всех жанров...");
        List<Genre> genres = genreService.getAll();
        log.info("Список всех жанров сформирован.");

        return genres;
    }

    @GetMapping("/{id}")
    private Genre get(@PathVariable long id) {
        log.info("Поиск жанра c id={}...", id);
        Genre genre = genreService.find(id);
        log.info("Жанр с id={} найден.", id);

        return genre;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void remove(@PathVariable long id) {
        log.info("Удаление жанра с id={}...", id);
        genreService.remove(id);
        log.info("Удален жанр c id={}.", id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void removeAll() {
        log.info("Удаление всех жанров...");
        genreService.removeAll();
        log.info("Все жанры удалены.");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Genre create(@RequestBody @Valid Genre genre) {
        log.info("Создание нового жанра {}...", genre);
        Genre createdGenre = genreService.create(genre);
        log.info("Создан новый жанр {}.", createdGenre);

        return createdGenre;
    }

    @PutMapping
    private Genre update(@RequestBody @Validated({RestValidationGroups.Update.class, Default.class}) Genre genre) {
        log.info("Обновление фильма {}...", genre);
        Genre updatedGenre = genreService.update(genre);
        log.info("Обновлен фильм {}.", updatedGenre);

        return updatedGenre;
    }
}
