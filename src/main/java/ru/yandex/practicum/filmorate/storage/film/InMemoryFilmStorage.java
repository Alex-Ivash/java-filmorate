package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private long seq = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(++seq);
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Optional<Film> find(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film update(Film updatedFilm) {
        films.replace(updatedFilm.getId(), updatedFilm);

        return updatedFilm;
    }

    @Override
    public void remove(Long id) {
        films.remove(id);
    }

    @Override
    public void removeAll() {
        films.clear();
    }
}
