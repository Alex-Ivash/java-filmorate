package ru.yandex.practicum.filmorate.repository.entity.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.JdbcBaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository extends JdbcBaseRepository<Genre> implements GenreRepository {
    private final JdbcClient jdbcClient;

    private final String CREATE_QUERY = "INSERT INTO %s (name) VALUES (:name)".formatted(getTableName());
    private final String UPDATE_QUERY = "UPDATE %s SET name=:name WHERE id=:id".formatted(getTableName());

    @Override
    public Genre create(Genre entity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(CREATE_QUERY)
                .param("name", entity.getName())
                .update(keyHolder, "id");

        entity.setId(keyHolder.getKeyAs(Long.class));

        return entity;
    }

    @Override
    public Genre update(Genre updatedEntity) {
        jdbcClient.sql(UPDATE_QUERY)
                .paramSource(updatedEntity)
                .update();

        return updatedEntity;
    }

    @Override
    public Optional<Genre> find(long id) {
        return jdbcClient.sql(FIND_BY_ID_QUERY)
                .param("id", id)
                .query(Genre.class)
                .optional();
    }

    @Override
    public List<Genre> findAll() {
        return jdbcClient.sql(FIND_ALL_QUERY)
                .query(Genre.class)
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
        return "genres";
    }
}
