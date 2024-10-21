package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.entity.user.UserRepository;
import ru.yandex.practicum.filmorate.repository.relation.user_friend.UsersFriendshipsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UsersFriendshipsRepository usersFriendshipsRepository;

    @Override
    public User create(User newUser) {
        newUser.setName(computeUserName(newUser));

        return userRepository.create(newUser);
    }

    @Override
    public User find(long id) {
        return checkUserExistence(id);
    }

    @Override
    public User update(User newUser) {
        checkUserExistence(newUser.getId());

        newUser.setName(computeUserName(newUser));
        return userRepository.update(newUser);
    }

    @Override
    public void remove(long id) {
        userRepository.remove(id);
    }

    @Override
    public void removeAll() {
        userRepository.removeAll();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        checkUserExistence(userId);
        checkUserExistence(friendId);

        usersFriendshipsRepository.insert(userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        checkUserExistence(userId);
        checkUserExistence(friendId);

        usersFriendshipsRepository.remove(userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        checkUserExistence(userId);

        return usersFriendshipsRepository.find(userId)
                .stream()
                .map(this::find)
                .toList();
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return usersFriendshipsRepository.findCommonFriends(userId, friendId)
                .stream()
                .map(this::find)
                .toList();
    }

    private String computeUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя {} пустое, заменено на его логин={}", user, user.getLogin());

            return user.getLogin();
        }

        return user.getName();
    }

    private User checkUserExistence(long id) {
        return userRepository.find(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }
}
