package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int seq = 0;

    @PostMapping
    private User createUser(@RequestBody @Valid User user) {
        if (user.getLogin().contains(" ")) {
            ResponseStatusException responseStatusException = new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Логин не может содержать пробелы"
            );

            log.warn(responseStatusException.getMessage());
            throw responseStatusException;
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(++seq);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: " + user);

        return user;
    }

    @PutMapping
    private User updateUser(@RequestBody @Valid User user) {
        if (user.getId() == null) {
            ResponseStatusException responseStatusException = new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Id должен быть указан"
            );

            log.warn(responseStatusException.getMessage());
            throw responseStatusException;
        }

        if (!users.containsKey(user.getId())) {
            ResponseStatusException responseStatusException = new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Пользователь с id = " + user.getId() + " не найден"
            );

            log.warn(responseStatusException.getMessage());
            throw responseStatusException;
        }

        users.put(user.getId(), user);
        log.info("Обновлен пользователь: " + user);

        return user;
    }

    @GetMapping
    private Collection<User> getUsers() {
        return users.values();
    }
}
