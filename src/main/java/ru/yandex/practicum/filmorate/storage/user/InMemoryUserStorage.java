package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private long seq = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(++seq);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User find(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User updatedUser) {
        find(updatedUser.getId());
        users.replace(updatedUser.getId(), updatedUser);

        return updatedUser;
    }

    @Override
    public void remove(Long id) {
        find(id);
        users.remove(id);
    }

    @Override
    public void removeAll() {
        users.clear();
    }
}
