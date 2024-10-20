package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.BaseService;

import java.util.List;

public interface FilmService extends BaseService<Film> {

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getPopular(int count);
}
