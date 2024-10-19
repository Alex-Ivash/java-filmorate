package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    private User get(@PathVariable long id) {
        log.info("Поиск пользователя c id={}...", id);
        User user = userService.find(id);
        log.info("Пользователь c id={} найден.", id);

        return user;
    }

    @GetMapping
    private List<User> getAll() {
        log.info("Формирование списка всех пользователей...");
        List<User> allUsers = userService.getAll();
        log.info("Список всех пользователей сформирован.");

        return allUsers;
    }

    @GetMapping("/{id}/friends")
    private List<User> getFriends(@PathVariable long id) {
        log.info("Формирование списка друзей пользователя с id={}...", id);
        List<User> userFriends = userService.getAllFriends(id);
        log.info("Список друзей пользователя с id={} сформирован.", id);

        return userFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    private List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Формирование списка общих друзей пользователей с id={} и id={}...", id, otherId);
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Список общих друзей пользователей с id={} и id={} сформирован.", id, otherId);

        return commonFriends;
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Создание дружбы между пользователями с id={} и id={}...", id, friendId);
        userService.addFriend(id, friendId);
        log.info("Дружба между пользователями с id={} и id={} создана.", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Удаление дружбы между пользователями с id={} и id={}...", id, friendId);
        userService.removeFriend(id, friendId);
        log.info("Дружба между пользователями с id={} и id={} удалена.", id, friendId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private User create(@RequestBody @Valid User user) {
        log.info("Создание нового пользователя {}...", user);
        User createdUser = userService.create(user);
        log.info("Создан новый пользователь {}.", createdUser);

        return createdUser;
    }

    @PutMapping
    private User update(@RequestBody @Validated({RestValidationGroups.Update.class, Default.class}) User user) {
        log.info("Обновление пользователя {}...", user);
        User updatedUser = userService.update(user);
        log.info("Обновлен пользователь {}.", updatedUser);

        return updatedUser;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void remove(@PathVariable long id) {
        log.info("Удаление пользователя с id={}...", id);
        userService.remove(id);
        log.info("Удален пользователь c id={}.", id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void remove() {
        log.info("Удаление всех пользователей...");
        userService.removeAll();
        log.info("Все пользователи удалены.");
    }
}
