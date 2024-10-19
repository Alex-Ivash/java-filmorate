package ru.yandex.practicum.filmorate.repository.relation;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRelationRepository {
    Set<Long> find(long id);

    Map<Long, Set<Long>> findAll();

    void removeAll();

    void removeAllById(long id);

    void insertAny(long id, Set<Long> idsList);

    void insert(long id, long relationId);

    void update(long id, Set<Long> idsList);

    void remove(long id, long relationId);

    default void batchUpdate(NamedParameterJdbcOperations namedParameterJdbcOperations, List<Map<String, Long>> params, String query) {
        namedParameterJdbcOperations.batchUpdate(
                query,
                params.stream()
                        .map(MapSqlParameterSource::new)
                        .toArray(SqlParameterSource[]::new)
        );
    }
}
