package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film find(long id);

    Film update(Film newFilm);

    List<Film> getAll();

    void remove(long id);

    void addLike(Film film, User user);

    void removeLike(Film film, User user);

    List<Film> getPopular(int count);
}
