package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.entity.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.entity.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.entity.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.repository.entity.user.UserRepository;
import ru.yandex.practicum.filmorate.repository.relation.film_genre.FilmsGenresRepository;
import ru.yandex.practicum.filmorate.repository.relation.film_like.FilmsLikesRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final FilmsGenresRepository filmGenresRepository;
    private final FilmsLikesRepository filmsLikesRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final UserRepository userRepository;

    @Override
    public Film create(Film newFilm) {
        if (newFilm.getMpa() != null) {
            newFilm.setMpa(checkMpaExistence(newFilm.getMpa().getId()));
        }

        Film createdFilm = filmRepository.create(newFilm);

        if (newFilm.getGenres() != null) {
            Set<Long> genresIds = newFilm.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .collect(Collectors.toSet());
            newFilm.setGenres(getFilmGenresIfExistingInRepo(genresIds));
            filmGenresRepository.update(createdFilm.getId(), genresIds);
        }

        return createdFilm;
    }

    @Override
    public Film find(long id) {
        Film film = checkFilmExistence(id);
        Set<Long> filmGenres = filmGenresRepository.find(id);

        if (film.getMpa() != null) {
            film.setMpa(checkMpaExistence(film.getMpa().getId()));
        }

        film.setGenres(getFilmGenresIfExistingInRepo(filmGenres));

        return film;
    }

    @Override
    public Film update(Film updatedFilm) {
        checkFilmExistence(updatedFilm.getId());

        if (updatedFilm.getMpa() != null) {
            Mpa mpa = checkMpaExistence(updatedFilm.getMpa().getId());
            updatedFilm.setMpa(mpa);
        }

        if (updatedFilm.getGenres() != null) {
            Set<Long> genresIds = updatedFilm.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .collect(Collectors.toSet());
            Set<Genre> genres = getFilmGenresIfExistingInRepo(genresIds);

            updatedFilm.getGenres().addAll(genres);
            filmGenresRepository.update(updatedFilm.getId(), genresIds);
        }


        return filmRepository.update(updatedFilm);
    }

    @Override
    public List<Film> getAll() {
        Map<Long, Mpa> allMpasMap = getAllMpas();
        Map<Long, Genre> allGenresMap = getAllGenres();
        Map<Long, Set<Genre>> allFilmGenresMap = getAllFilmGenres(allGenresMap);

        return filmRepository.findAll().stream()
                .peek(film -> {
                    film.getGenres().addAll(allFilmGenresMap.getOrDefault(film.getId(), Set.of()));
                    film.setMpa(allMpasMap.getOrDefault(film.getMpa().getId(), null));
                })
                .toList();
    }

    @Override
    public void remove(long id) {
        filmRepository.remove(id);
    }

    @Override
    public void removeAll() {
        filmRepository.removeAll();
    }

    @Override
    public void addLike(long filmId, long userId) {
        checkFilmExistence(filmId);
        checkUserExistence(userId);

        filmsLikesRepository.insert(filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        checkFilmExistence(filmId);
        checkUserExistence(userId);

        filmsLikesRepository.remove(filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return getAll()
                .stream()
                .sorted(Comparator.comparingInt((Film film) ->
                        filmsLikesRepository.find(film.getId()).size()).reversed())
                .limit(count)
                .toList();
    }

    private Map<Long, Mpa> getAllMpas() {
        return mpaRepository.findAll().stream()
                .collect(Collectors.toMap(Mpa::getId, mpa -> mpa));
    }

    private Map<Long, Genre> getAllGenres() {
        return genreRepository.findAll().stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre));
    }

    private Map<Long, Set<Genre>> getAllFilmGenres(Map<Long, Genre> allGenresMap) {
        return filmGenresRepository.findAll().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(allGenresMap::get)
                                .collect(Collectors.toSet())
                ));
    }

    private Set<Genre> getFilmGenresIfExistingInRepo(Set<Long> genresIds) {
        return genresIds.stream()
                .map(this::checkGenreExistence)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Genre::getId))));
    }

    private Mpa checkMpaExistence(long id) {
        return mpaRepository.find(id)
                .orElseThrow(() -> new NotValidException("Mpa с id=" + id + " не найден"));
    }

    private User checkUserExistence(long id) {
        return userRepository.find(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    private Film checkFilmExistence(long id) {
        return filmRepository.find(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }

    private Genre checkGenreExistence(long id) {
        return genreRepository.find(id)
                .orElseThrow(() -> new NotValidException("Жанр с id=" + id + " не найден"));
    }
}
