package ru.yandex.practicum.filmorate.repository.entity.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcGenreRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcGenreRepositoryTest {
    private final JdbcGenreRepository jdbcGenreRepository;

    static Genre getTestEntity1() {
        return new Genre(
                1L,
                "Комедия"
        );
    }

    static Genre getTestEntity2() {
        return new Genre(
                2L,
                "Драма"
        );
    }

    static Genre getTestEntity3() {
        return new Genre(
                3L,
                "Мультфильм"
        );
    }

    static Genre getTestEntity4() {
        return new Genre(
                4L,
                "Триллер"
        );
    }

    static Genre getTestEntityWithNoEquivalentInDB() {
        return new Genre(
                null,
                "Новый тестовый жанр"
        );
    }

    @Test
    @DisplayName("Создание новой сущности")
    void create_entityCreatedInDB() {
        //given
        Genre expectedEntity = getTestEntityWithNoEquivalentInDB();
        expectedEntity.setId(5L);

        //when
        jdbcGenreRepository.create(getTestEntityWithNoEquivalentInDB());

        //then
        Optional<Genre> createdEntity = jdbcGenreRepository.find(5L);
        assertThat(createdEntity)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedEntity);
    }

    @Test
    @DisplayName("Обновление существующей сущности")
    void update_entityUpdatedInDB() {
        //given
        Genre changedEntity = getTestEntityWithNoEquivalentInDB();
        changedEntity.setId(1L);

        //when
        jdbcGenreRepository.update(changedEntity);

        //then
        Optional<Genre> updatedEntity = jdbcGenreRepository.find(1L);
        assertThat(updatedEntity)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(changedEntity);
    }

    @Test
    @DisplayName("Возврат сущности по id")
    void find_entityReturnsFromDB() {
        //when
        Optional<Genre> entity = jdbcGenreRepository.find(1L);

        //then
        assertThat(entity)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestEntity1());
    }

    @Test
    @DisplayName("Возврат всех сущностей")
    void findAll_allEntityReturnsFromDB() {
        //given
        List<Genre> expectedEntities = List.of(getTestEntity1(), getTestEntity2(), getTestEntity3(), getTestEntity4());

        //when
        List<Genre> allFoundEntities = jdbcGenreRepository.findAll();

        //then
        assertThat(allFoundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Удаление сущности по id")
    void remove_entityRemovedFromDB() {
        //when
        jdbcGenreRepository.remove(1L);

        //then
        Optional<Genre> entity = jdbcGenreRepository.find(1L);
        assertThat(entity).isNotPresent();
    }

    @Test
    @DisplayName("Удаление всех сущностей")
    void removeAll_allEntityRemoverFromDB() {
        //when
        jdbcGenreRepository.removeAll();

        //then
        List<Genre> allEntities = jdbcGenreRepository.findAll();
        assertThat(allEntities).isEmpty();
    }
}
