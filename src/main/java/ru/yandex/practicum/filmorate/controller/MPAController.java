package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mpa")
public class MPAController {
    private final MpaService mpaService;

    @GetMapping
    private List<Mpa> getAll() {
        log.info("Формирование списка всех рейтингов...");
        List<Mpa> mpas = mpaService.getAll();
        log.info("Список всех рейтингов сформирован.");

        return mpas;
    }

    @GetMapping("/{id}")
    private Mpa get(@PathVariable long id) {
        log.info("Поиск рейтинга c id={}...", id);
        Mpa mpa = mpaService.find(id);
        log.info("Рейтинг с id={} найден.", id);

        return mpa;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void remove(@PathVariable long id) {
        log.info("Удаление рейтинга с id={}...", id);
        mpaService.remove(id);
        log.info("Удален рейтинг c id={}.", id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void removeAll() {
        log.info("Удаление всех рейтингов...");
        mpaService.removeAll();
        log.info("Все рейтинги удалены.");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Mpa create(@RequestBody @Valid Mpa mpa) {
        log.info("Создание нового рейтинга {}...", mpa);
        Mpa createdMpa = mpaService.create(mpa);
        log.info("Создан новый рейтинг {}.", createdMpa);

        return createdMpa;
    }

    @PutMapping
    private Mpa update(@RequestBody @Validated({RestValidationGroups.Update.class, Default.class}) Mpa mpa) {
        log.info("Обновление рейтинга {}...", mpa);
        Mpa updatedMpa = mpaService.update(mpa);
        log.info("Обновлен рейтинг {}.", updatedMpa);

        return updatedMpa;
    }
}
