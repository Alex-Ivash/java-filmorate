package ru.yandex.practicum.filmorate.repository.relation.user_friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcUsersFriendshipsRepository implements UsersFriendshipsRepository {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final JdbcClient jdbcClient;

    private static final String TABLE_NAME = "users_friendships";
    private static final String FIND_BY_ID_QUERY = "SELECT friend_id FROM " + TABLE_NAME + " WHERE user_id=:userId";
    private static final String FIND_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private static final String FIND_COMMON_FRIENDS_QUERY = """
            SELECT DISTINCT friend_id
            FROM %s
            WHERE user_id IN (:userId, :friendId)
            GROUP BY friend_id
            HAVING COUNT(*) = 2;
            """.formatted(TABLE_NAME);
    private static final String REMOVE_ALL_QUERY = "DELETE FROM " + TABLE_NAME;
    private static final String REMOVE_ALL_USER_FRIENDS_QUERY = """
            DELETE FROM %s
            WHERE user_id=:userId;
            """.formatted(TABLE_NAME);
    private static final String REMOVE_FRIENDSHIP_QUERY = """
            DELETE FROM %s
            WHERE user_id = :userId AND friend_id = :friendId;
            """.formatted(TABLE_NAME);
    private static final String CREATE_QUERY = """
            INSERT INTO %s (user_id, friend_id)
            VALUES (:userId, :friendId)
            """.formatted(TABLE_NAME);


    @Override
    public Set<Long> find(long id) {
        return jdbcClient.sql(FIND_BY_ID_QUERY)
                .param("userId", id)
                .query(Long.class)
                .set();
    }

    @Override
    public Map<Long, Set<Long>> findAll() {
        Map<Long, Set<Long>> result = new HashMap<>();

        jdbcClient.sql(FIND_ALL_QUERY)
                .query(rch -> {
                    Long id = rch.getLong("user_id");
                    Long otherId = rch.getLong("friend_id");

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
                                "userId", id,
                                "friendId", secId))
                        .toList(),
                CREATE_QUERY
        );
    }

    @Override
    public void insert(long id, long relationId) {
        jdbcClient.sql(CREATE_QUERY)
                .param("userId", id)
                .param("friendId", relationId)
                .update();
    }

    @Override
    public void update(long id, Set<Long> idsList) {
        removeAllById(id);
        insertAny(id, idsList);
    }

    @Override
    public void remove(long id, long relationId) {
        jdbcClient.sql(REMOVE_FRIENDSHIP_QUERY)
                .param("userId", id)
                .param("friendId", relationId)
                .update();
    }

    @Override
    public void removeAllById(long id) {
        jdbcClient.sql(REMOVE_ALL_USER_FRIENDS_QUERY)
                .param("userId", id)
                .update();
    }

    @Override
    public void removeAll() {
        jdbcClient.sql(REMOVE_ALL_QUERY)
                .update();
    }

    @Override
    public List<Long> findCommonFriends(long userId, long otherUserId) {
        return jdbcClient.sql(FIND_COMMON_FRIENDS_QUERY)
                .param("userId", userId)
                .param("friendId", otherUserId)
                .query(Long.class)
                .list();
    }
}
