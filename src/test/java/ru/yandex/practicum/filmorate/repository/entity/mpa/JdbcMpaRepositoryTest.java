package ru.yandex.practicum.filmorate.repository.entity.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcMpaRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcMpaRepositoryTest {
    private final JdbcMpaRepository jdbcMpaRepository;

    static Mpa getTestEntity1() {
        return new Mpa(
                1L,
                "G"
        );
    }

    static Mpa getTestEntity2() {
        return new Mpa(
                2L,
                "PG"
        );
    }

    static Mpa getTestEntityWithNoEquivalentInDB() {
        return new Mpa(
                null,
                "Новый тестовый рейтинг"
        );
    }

    @Test
    @DisplayName("Создание новой сущности")
    void create_entityCreatedInDB() {
        //given
        Mpa expectedEntity = getTestEntityWithNoEquivalentInDB();
        expectedEntity.setId(3L);

        //when
        jdbcMpaRepository.create(getTestEntityWithNoEquivalentInDB());

        //then
        Optional<Mpa> createdEntity = jdbcMpaRepository.find(3L);
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
        Mpa changedEntity = getTestEntityWithNoEquivalentInDB();
        changedEntity.setId(1L);

        //when
        jdbcMpaRepository.update(changedEntity);

        //then
        Optional<Mpa> updatedEntity = jdbcMpaRepository.find(1L);
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
        Optional<Mpa> entity = jdbcMpaRepository.find(1L);

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
        List<Mpa> expectedEntities = List.of(getTestEntity1(), getTestEntity2());

        //when
        List<Mpa> allFoundEntities = jdbcMpaRepository.findAll();

        //then
        assertThat(allFoundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Удаление сущности по id")
    void remove_entityRemovedFromDB() {
        //when
        jdbcMpaRepository.remove(1L);

        //then
        Optional<Mpa> entity = jdbcMpaRepository.find(1L);
        assertThat(entity).isNotPresent();
    }

    @Test
    @DisplayName("Удаление всех сущностей")
    void removeAll_allEntityRemoverFromDB() {
        //when
        jdbcMpaRepository.removeAll();

        //then
        List<Mpa> allEntities = jdbcMpaRepository.findAll();
        assertThat(allEntities).isEmpty();
    }

}
