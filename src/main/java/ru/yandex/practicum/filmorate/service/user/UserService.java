package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    User find(long id);

    User update(User newUser);

    void remove(long id);

    List<User> getAll();

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getAllFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long anotherUserId);
}
