package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.entity.genre.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Genre create(Genre entity) {
        return genreRepository.create(entity);
    }

    @Override
    public Genre find(long id) {
        return checkGenreExistence(id);
    }

    @Override
    public Genre update(Genre newEntity) {
        return genreRepository.update(newEntity);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public void remove(long id) {
        genreRepository.remove(id);
    }

    @Override
    public void removeAll() {
        genreRepository.removeAll();
    }

    private Genre checkGenreExistence(long id) {
        return genreRepository.find(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id=" + id + " не найден"));
    }
}
