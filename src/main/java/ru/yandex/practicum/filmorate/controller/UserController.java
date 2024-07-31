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
        return userService.find(id);
    }

    @GetMapping
    private List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}/friends")
    private List<User> getFriends(@PathVariable long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    private List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private User create(@RequestBody @Valid User user) {
        log.info("Попытка создать нового пользователя {}", user);
        User createdUser = userService.create(user);
        log.info("Создан новый пользователь {}", createdUser);

        return createdUser;
    }

    @PutMapping
    private User update(@RequestBody @Validated({RestValidationGroups.Update.class, Default.class}) User user) {
        log.info("Попытка обновить пользователя {}", user);

        User updatedUser = userService.update(user);
        log.info("Обновлен пользователь {}", updatedUser);

        return updatedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    private void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Попытка подружить пользователей с id={} и id={}", id, friendId);

        userService.addFriend(id, friendId);
        log.info("Пользователи с id={} и id={} подружились", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Попытка удалить друг друга из друзей для пользователей с id={} и id={}", id, friendId);

        userService.removeFriend(id, friendId);
        log.info("Пользователи id={} и id={} больше не друзья", id, friendId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void remove(@PathVariable long id) {
        log.info("Попытка удалить пользователя с id={}", id);

        userService.remove(id);
        log.info("Удален пользователь c id={}", id);
    }
}
