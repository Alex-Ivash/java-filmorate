package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    T create(T entity);

    Optional<T> find(long id);

    List<T> findAll();

    T update(T updatedEntity);

    void remove(long id);

    void removeAll();
}
