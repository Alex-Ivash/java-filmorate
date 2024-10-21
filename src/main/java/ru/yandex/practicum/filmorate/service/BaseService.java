package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface BaseService<T> {
    T create(T film);

    T find(long id);

    T update(T newFilm);

    List<T> getAll();

    void remove(long id);

    void removeAll();
}
