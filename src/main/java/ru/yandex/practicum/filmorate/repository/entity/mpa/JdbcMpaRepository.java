package ru.yandex.practicum.filmorate.repository.entity.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.JdbcBaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository extends JdbcBaseRepository<Mpa> implements MpaRepository {
    private final JdbcClient jdbcClient;

    private final String CREATE_QUERY = "INSERT INTO %s (name) VALUES (:name)".formatted(getTableName());
    private final String UPDATE_QUERY = "UPDATE %s SET name=:name WHERE id=:id".formatted(getTableName());

    @Override
    public Mpa create(Mpa entity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(CREATE_QUERY)
                .param("name", entity.getName())
                .update(keyHolder, "id");

        entity.setId(keyHolder.getKeyAs(Long.class));

        return entity;
    }

    @Override
    public Mpa update(Mpa updatedEntity) {
        jdbcClient.sql(UPDATE_QUERY)
                .paramSource(updatedEntity)
                .update();

        return updatedEntity;
    }

    @Override
    public Optional<Mpa> find(long id) {
        return jdbcClient.sql(FIND_BY_ID_QUERY)
                .param("id", id)
                .query(Mpa.class)
                .optional();
    }

    @Override
    public List<Mpa> findAll() {
        return jdbcClient.sql(FIND_ALL_QUERY)
                .query(Mpa.class)
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
        return "mpa";
    }
}
