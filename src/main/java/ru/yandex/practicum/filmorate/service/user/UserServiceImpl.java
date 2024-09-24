package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User create(User user) {
        user.setName(computeUserName(user));

        return userStorage.create(user);
    }

    @Override
    public User find(long id) {
        return userStorage.find(id);
    }

    @Override
    public User update(User newUser) {
        find(newUser.getId());
        newUser.setName(computeUserName(newUser));

        return userStorage.update(newUser);
    }

    @Override
    public void remove(long id) {
        find(id);
        userStorage.remove(id);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        Set<Long> userFriends = find(userId).getFriends();
        Set<Long> anotherUserFriends = find(friendId).getFriends();

        userFriends.add(friendId);
        anotherUserFriends.add(userId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        Set<Long> userFriends = find(userId).getFriends();
        Set<Long> anotherUserFriends = find(friendId).getFriends();

        userFriends.remove(friendId);
        anotherUserFriends.remove(userId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        return find(userId)
                .getFriends()
                .stream()
                .map(this::find)
                .toList();
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long anotherUserId) {
        Set<Long> userFriends = find(userId).getFriends();
        Set<Long> anotherUserFriends = find(anotherUserId).getFriends();

        userFriends.retainAll(anotherUserFriends);

        return userFriends
                .stream()
                .map(this::find)
                .toList();
    }

    private String computeUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя {} пустое, поэтому заменено на его логин {}", user, user.getLogin());

            return user.getLogin();
        }

        return user.getName();
    }
}
