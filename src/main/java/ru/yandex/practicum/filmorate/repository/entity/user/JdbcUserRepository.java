package ru.yandex.practicum.filmorate.repository.entity.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final JdbcClient jdbcClient;

    private static final String TABLE_NAME = "users";
    private static final String CREATE_QUERY = """
            INSERT INTO %s (email, login, name, birthday)
            VALUES (:email, :login, :name, :birthday)
            """.formatted(TABLE_NAME);
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id=:id";
    private static final String REMOVE_BY_ID_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id=:id";
    private static final String REMOVE_ALL_QUERY = "DELETE FROM " + TABLE_NAME;
    private static final String FIND_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private static final String UPDATE_QUERY = """
            UPDATE %s SET
               email=:email,
               login=:login,
               name=:name,
               birthday=:birthday
            WHERE id = :id
            """.formatted(TABLE_NAME);

    @Override
    public User create(User newEntity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(CREATE_QUERY)
                .paramSource(newEntity)
                .update(keyHolder, "id");

        newEntity.setId(keyHolder.getKeyAs(Long.class));

        return newEntity;
    }

    @Override
    public User update(User updatedEntity) {
        jdbcClient.sql(UPDATE_QUERY)
                .paramSource(updatedEntity)
                .update();

            return updatedEntity;
    }

    @Override
    public Optional<User> find(long id) {
        return jdbcClient.sql(FIND_BY_ID_QUERY)
                .param("id", id)
                .query(User.class)
                .optional();
    }

    @Override
    public List<User> findAll() {
        return jdbcClient.sql(FIND_ALL_QUERY)
                .query(User.class)
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
}
