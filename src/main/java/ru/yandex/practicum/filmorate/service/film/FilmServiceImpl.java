package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film find(long id) {
        return filmStorage.find(id);
    }

    @Override
    public Film update(Film newFilm) {
        find(newFilm.getId());

        return filmStorage.update(newFilm);
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public void remove(long id) {
        find(id);
        filmStorage.remove(id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = filmStorage.find(filmId);
        User user = userStorage.find(userId);

        film.getLikes().add(user.getId());
    }

    @Override
    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.find(filmId);
        User user = userStorage.find(userId);

        film.getLikes().remove(user.getId());
    }

    @Override
    public List<Film> getPopular(int count) {
        return getAll()
                .stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .toList();
    }
}
