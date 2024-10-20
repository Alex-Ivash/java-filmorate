package ru.yandex.practicum.filmorate.repository;


public abstract class JdbcBaseRepository<T> implements BaseRepository<T> {
    protected final String FIND_BY_ID_QUERY = "SELECT * FROM %s WHERE id=:id".formatted(getTableName());
    protected final String FIND_ALL_QUERY = "SELECT * FROM %s ORDER BY id ASC".formatted(getTableName());
    protected final String REMOVE_BY_ID_QUERY = "DELETE FROM %s WHERE id=:id".formatted(getTableName());
    protected final String REMOVE_ALL_QUERY = "DELETE FROM %s".formatted(getTableName());

    protected abstract String getTableName();
}
