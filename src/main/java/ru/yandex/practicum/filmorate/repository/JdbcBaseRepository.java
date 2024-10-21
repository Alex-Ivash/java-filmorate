package ru.yandex.practicum.filmorate.repository;


public abstract class JdbcBaseRepository<T> implements BaseRepository<T> {
    protected final String findByIdQuery = "SELECT * FROM %s WHERE id=:id".formatted(getTableName());
    protected final String findAllQuery = "SELECT * FROM %s ORDER BY id ASC".formatted(getTableName());
    protected final String removeByIdQuery = "DELETE FROM %s WHERE id=:id".formatted(getTableName());
    protected final String removeAllQuery = "DELETE FROM %s".formatted(getTableName());

    protected abstract String getTableName();
}
