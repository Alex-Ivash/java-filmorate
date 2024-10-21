package ru.yandex.practicum.filmorate.repository.relation.film_genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcFilmsGenresRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmsGenresRepositoryTest {
    private final JdbcFilmsGenresRepository jdbcFilmsGenresRepository;

    @Test
    @DisplayName("Возврат всех id сущностей, связанных с переданным id")
    void find_allRelationsByIdReturnsFromDB() {
        //when
        Set<Long> relatedIds = jdbcFilmsGenresRepository.find(1L);

        //then
        assertThat(relatedIds)
                .usingRecursiveComparison()
                .isEqualTo(Set.of(1L, 2L));
    }

    @Test
    @DisplayName("Возврат всех связей")
    void findAll_allRelationsReturnsFromDB() {
        //given
        Map<Long, Set<Long>> expectedEntities = Map.of(1L, Set.of(1L, 2L), 2L, Set.of(2L));

        //when
        Map<Long, Set<Long>> allFoundEntities = jdbcFilmsGenresRepository.findAll();

        //then
        assertThat(allFoundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Добавление одной конкретной связи")
    void insert() {
        //given
        Set<Long> expectedEntities = Set.of(2L, 1L);

        //when
        jdbcFilmsGenresRepository.insert(2L, 1L);

        //then
        Set<Long> foundEntities = jdbcFilmsGenresRepository.find(2L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Добавление нескольких связей сущности")
    void insertAny() {
        //given
        Set<Long> expectedEntities = Set.of(1L, 2L);

        //when
        jdbcFilmsGenresRepository.insertAny(3L, expectedEntities);

        //then
        Set<Long> foundEntities = jdbcFilmsGenresRepository.find(3L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Обновление всех связей сущности")
    void update() {
        //given
        Set<Long> expectedEntities = Set.of(3L, 4L);

        //when
        jdbcFilmsGenresRepository.update(3L, expectedEntities);

        //then
        Set<Long> foundEntities = jdbcFilmsGenresRepository.find(3L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Удаление одной конкретной связи")
    void remove() {
        //given
        Set<Long> expectedEntities = Set.of(2L);

        //when
        jdbcFilmsGenresRepository.remove(1L, 1L);

        //then
        Set<Long> foundEntities = jdbcFilmsGenresRepository.find(1L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Удаление всех связей конкретной сущности")
    void removeAllById_allRelationsByIdRemovedFromDB() {
        //when
        jdbcFilmsGenresRepository.removeAllById(1L);

        //then
        Set<Long> relatedIds = jdbcFilmsGenresRepository.find(1L);
        assertThat(relatedIds).isEmpty();
    }

    @Test
    @DisplayName("Удаление всех связей")
    void removeAll_allRelationsRemovedFromDB() {
        //when
        jdbcFilmsGenresRepository.removeAll();

        //then
        Map<Long, Set<Long>> allEntities = jdbcFilmsGenresRepository.findAll();
        assertThat(allEntities).isEmpty();
    }
}
