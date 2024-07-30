package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface BaseStorage<T, ID> {
    T create(T entity);

    Optional<T> find(ID id);

    List<T> getAll();

    T update(T updatedEntity);

    void remove(ID id);

    void removeAll();
}
