package ru.yandex.practicum.filmorate.repository.entity.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.JdbcBaseRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository extends JdbcBaseRepository<Film> implements FilmRepository {
    private final JdbcClient jdbcClient;

    private final String CREATE_QUERY = """
            INSERT INTO %s (name, description, release_date, duration, mpa)
            VALUES (:name, :description, :releaseDate, :duration, :mpa)
            """.formatted(getTableName());
    private final String UPDATE_QUERY = """
            UPDATE %s SET
               name=:name,
               description=:description,
               release_date=:releaseDate,
               duration=:duration,
               mpa=:mpa
            WHERE id=:id
            """.formatted(getTableName());

    @Override
    public Film create(Film newEntity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(CREATE_QUERY)
                .param("name", newEntity.getName())
                .param("description", newEntity.getDescription())
                .param("releaseDate", newEntity.getReleaseDate())
                .param("duration", newEntity.getDuration())
                .param("mpa", newEntity.getMpa().getId())
                .update(keyHolder, "id");

        newEntity.setId(keyHolder.getKeyAs(Long.class));

        return newEntity;
    }

    @Override
    public Film update(Film updatedEntity) {
        jdbcClient.sql(UPDATE_QUERY)
                .param("id", updatedEntity.getId())
                .param("name", updatedEntity.getName())
                .param("description", updatedEntity.getDescription())
                .param("releaseDate", updatedEntity.getReleaseDate())
                .param("duration", updatedEntity.getDuration())
                .param("mpa", updatedEntity.getMpa().getId())
                .update();

        return updatedEntity;
    }

    @Override
    public Optional<Film> find(long id) {
        return jdbcClient.sql(FIND_BY_ID_QUERY)
                .param("id", id)
                .query((rm, n) -> new Film(
                        rm.getLong("id"),
                        new LinkedHashSet<>(),
                        rm.getString("name"),
                        rm.getString("description"),
                        rm.getDate("release_date").toLocalDate(),
                        rm.getInt("duration"),
                        new Mpa(rm.getLong("mpa"), null)
                ))
                .optional();
    }

    @Override
    public List<Film> findAll() {
        return jdbcClient.sql(FIND_ALL_QUERY)
                .query((rm, n) -> new Film(
                        rm.getLong("id"),
                        new LinkedHashSet<>(),
                        rm.getString("name"),
                        rm.getString("description"),
                        rm.getDate("release_date").toLocalDate(),
                        rm.getInt("duration"),
                        new Mpa(rm.getLong("mpa"), null)
                ))
                .list();
    }

    @Override
    public void remove(long id) {
        jdbcClient.sql(REMOVE_BY_ID_QUERY)
                .param("id", id)
                .update();
    }

    @Override
    public void removeAll() {
        jdbcClient.sql(REMOVE_ALL_QUERY)
                .update();
    }

    @Override
    protected String getTableName() {
        return "films";
    }
}
