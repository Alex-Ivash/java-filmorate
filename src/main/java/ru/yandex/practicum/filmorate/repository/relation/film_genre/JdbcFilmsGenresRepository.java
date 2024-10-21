package ru.yandex.practicum.filmorate.repository.relation.film_genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcFilmsGenresRepository implements FilmsGenresRepository {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final JdbcClient jdbcClient;

    private static final String TABLE_NAME = "films_genres";
    private static final String REMOVE_ALL_FILM_GENRES_BY_ID_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE film_id=:filmId";
    private static final String REMOVE_FILM_GENRE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE film_id=:filmId AND genre_id=:genreId";
    private static final String FIND_BY_ID_QUERY = "SELECT genre_id FROM " + TABLE_NAME + " WHERE film_id=:filmId";
    private static final String CREATE_QUERY = """
            INSERT INTO %s (film_id, genre_id)
            VALUES (:filmId, :genreId)
            """.formatted(TABLE_NAME);
    private static final String FIND_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private static final String REMOVE_ALL_QUERY = "DELETE FROM " + TABLE_NAME;

    @Override
    public Set<Long> find(long id) {
        return jdbcClient.sql(FIND_BY_ID_QUERY)
                .param("filmId", id)
                .query(Long.class)
                .set();
    }

    @Override
    public Map<Long, Set<Long>> findAll() {
        Map<Long, Set<Long>> result = new HashMap<>();

        jdbcClient.sql(FIND_ALL_QUERY)
                .query(rch -> {
                    Long id = rch.getLong("film_id");
                    Long otherId = rch.getLong("genre_id");

                    result.computeIfAbsent(id, k -> new HashSet<>()).add(otherId);
                });

        return result;
    }

    @Override
    public void insertAny(long id, Set<Long> idsList) {
        batchUpdate(
                namedParameterJdbcOperations,
                idsList.stream()
                        .map(secId -> Map.of(
                                "filmId", id,
                                "genreId", secId))
                        .toList(),
                CREATE_QUERY
        );
    }

    @Override
    public void insert(long id, long relationId) {
        jdbcClient.sql(CREATE_QUERY)
                .param("filmId", id)
                .param("genreId", relationId)
                .update();
    }

    @Override
    public void update(long id, Set<Long> idsList) {
        removeAllById(id);
        insertAny(id, idsList);
    }

    @Override
    public void remove(long id, long relationId) {
        jdbcClient.sql(REMOVE_FILM_GENRE_QUERY)
                .param("filmId", id)
                .param("genreId", relationId)
                .update();
    }

    @Override
    public void removeAllById(long id) {
        jdbcClient.sql(REMOVE_ALL_FILM_GENRES_BY_ID_QUERY)
                .param("filmId", id)
                .update();
    }

    @Override
    public void removeAll() {
        jdbcClient.sql(REMOVE_ALL_QUERY)
                .update();
    }
}
