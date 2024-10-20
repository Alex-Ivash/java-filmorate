package ru.yandex.practicum.filmorate.repository.entity.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcBaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository extends JdbcBaseRepository<User> implements UserRepository {
    private final JdbcClient jdbcClient;

    private final String createQuery = """
            INSERT INTO %s (email, login, name, birthday)
            VALUES (:email, :login, :name, :birthday)
            """.formatted(getTableName());
    private final String updateQuery = """
            UPDATE %s SET
               email=:email,
               login=:login,
               name=:name,
               birthday=:birthday
            WHERE id = :id
            """.formatted(getTableName());

    @Override
    public User create(User newEntity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(createQuery)
                .paramSource(newEntity)
                .update(keyHolder, "id");

        newEntity.setId(keyHolder.getKeyAs(Long.class));

        return newEntity;
    }

    @Override
    public User update(User updatedEntity) {
        jdbcClient.sql(updateQuery)
                .paramSource(updatedEntity)
                .update();

            return updatedEntity;
    }

    @Override
    public Optional<User> find(long id) {
        return jdbcClient.sql(findByIdQuery)
                .param("id", id)
                .query(User.class)
                .optional();
    }

    @Override
    public List<User> findAll() {
        return jdbcClient.sql(findAllQuery)
                .query(User.class)
                .list();
    }

    @Override
    public void remove(long id) {
        jdbcClient.sql(removeByIdQuery)
                .param("id", id)
                .update();
    }

    @Override
    public void removeAll() {
        jdbcClient.sql(removeAllQuery)
                .update();
    }

    @Override
    protected String getTableName() {
        return "users";
    }
}
