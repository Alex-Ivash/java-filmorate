package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

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
        user.setName(computeUserName(user));
        user.setId(++seq);
        users.put(user.getId(), user);

        log.info("Добавлен новый пользователь: " + user);

        return user;
    }

    @PutMapping
    private User updateUser(@RequestBody @Validated(RestValidationGroups.Update.class) User user) {
        checkUserExistence(user);
        user.setName(computeUserName(user));
        users.put(user.getId(), user);

        log.info("Обновлен пользователь: " + user);

        return user;
    }

    @GetMapping
    private Collection<User> getUsers() {
        return users.values();
    }

    private String computeUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            return user.getLogin();
        }

        return user.getName();
    }

    private void checkUserExistence(User user) {
        if (!users.containsKey(user.getId())) {
            String message = "Пользователь с id=" + user.getId() + " не найден";

            log.warn(message);

            throw new NotFoundException(message);
        }
    }
}
