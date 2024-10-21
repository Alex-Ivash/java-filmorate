package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.BaseService;

import java.util.List;

public interface UserService extends BaseService<User> {
    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getAllFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long anotherUserId);
}
