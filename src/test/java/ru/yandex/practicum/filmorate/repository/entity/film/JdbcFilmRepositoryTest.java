package ru.yandex.practicum.filmorate.repository.entity.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcFilmRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmRepositoryTest {
    private final JdbcFilmRepository jdbcFilmRepository;

    static Film getTestEntity1() {
        return new Film(
                1L,
                Collections.EMPTY_SET,
                "Inception",
                "A thief who steals corporate secrets through the use of dream-sharing technology.",
                LocalDate.of(2010, 7, 16),
                148,
                new Mpa(1L, null)
        );
    }

    static Film getTestEntity2() {
        return new Film(
                2L,
                Collections.EMPTY_SET,
                "The Matrix",
                "A computer hacker learns from mysterious rebels about the true nature of his reality.",
                LocalDate.of(1999, 3, 31),
                136,
                new Mpa(2L, null)
        );
    }

    static Film getTestEntity3() {
        return new Film(
                3L,
                Collections.EMPTY_SET,
                "Interstellar",
                "A team of explorers travel through a wormhole in space in an attempt to ensure humanity’s survival.",
                LocalDate.of(2014, 11, 7),
                148,
                new Mpa(1L, null)
        );
    }

    static Film getTestEntityWithNoEquivalentInDB() {
        return new Film(
                null,
                Collections.EMPTY_SET,
                "foo",
                "bar",
                LocalDate.of(2034, 11, 7),
                148,
                new Mpa(2L, null)
        );
    }

    @Test
    @DisplayName("Создание новой сущности")
    void create_entityCreatedInDB() {
        //given
        Film expectedEntity = getTestEntityWithNoEquivalentInDB();
        expectedEntity.setId(4L);

        //when
        jdbcFilmRepository.create(getTestEntityWithNoEquivalentInDB());

        //then
        Optional<Film> createdEntity = jdbcFilmRepository.find(4L);
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
        Film changedEntity = getTestEntityWithNoEquivalentInDB();
        changedEntity.setId(1L);

        //when
        jdbcFilmRepository.update(changedEntity);

        //then
        Optional<Film> updatedEntity = jdbcFilmRepository.find(1L);
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
        Optional<Film> entity = jdbcFilmRepository.find(1L);

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
        List<Film> expectedEntities = List.of(getTestEntity1(), getTestEntity2(), getTestEntity3());

        //when
        List<Film> allFoundEntities = jdbcFilmRepository.findAll();

        //then
        assertThat(allFoundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Удаление сущности по id")
    void remove_entityRemovedFromDB() {
        //when
        jdbcFilmRepository.remove(1L);

        //then
        Optional<Film> entity = jdbcFilmRepository.find(1L);
        assertThat(entity).isNotPresent();
    }

    @Test
    @DisplayName("Удаление всех сущностей")
    void removeAll_allEntityRemoverFromDB() {
        //when
        jdbcFilmRepository.removeAll();

        //then
        List<Film> allEntities = jdbcFilmRepository.findAll();
        assertThat(allEntities).isEmpty();
    }
}
