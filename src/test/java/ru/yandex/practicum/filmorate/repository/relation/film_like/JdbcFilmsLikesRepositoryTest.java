package ru.yandex.practicum.filmorate.repository.relation.film_like;

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
@Import(JdbcFilmsLikesRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmsLikesRepositoryTest {
    private final JdbcFilmsLikesRepository jdbcFilmsLikesRepository;

    @Test
    @DisplayName("Возврат всех id сущностей, связанных с переданным id")
    void find_allRelationsByIdReturnsFromDB() {
        //when
        Set<Long> relatedIds = jdbcFilmsLikesRepository.find(1L);

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
        Map<Long, Set<Long>> allFoundEntities = jdbcFilmsLikesRepository.findAll();

        //then
        assertThat(allFoundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Добавление одной конкретной связи")
    void insert() {
        //given
        Set<Long> expectedEntities = Set.of(2L, 3L);

        //when
        jdbcFilmsLikesRepository.insert(2L, 3L);

        //then
        Set<Long> foundEntities = jdbcFilmsLikesRepository.find(2L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Добавление нескольких связей сущности")
    void insertAny() {
        //given
        Set<Long> expectedEntities = Set.of(3L, 4L);

        //when
        jdbcFilmsLikesRepository.insertAny(3L, expectedEntities);

        //then
        Set<Long> foundEntities = jdbcFilmsLikesRepository.find(3L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Обновление всех связей сущности")
    void update() {
        //given
        Set<Long> expectedEntities = Set.of(2L, 3L);

        //when
        jdbcFilmsLikesRepository.update(1L, expectedEntities);

        //then
        Set<Long> foundEntities = jdbcFilmsLikesRepository.find(1L);
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
        jdbcFilmsLikesRepository.remove(1L, 1L);

        //then
        Set<Long> foundEntities = jdbcFilmsLikesRepository.find(1L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Удаление всех связей конкретной сущности")
    void removeAllById_allRelationsByIdRemovedFromDB() {
        //when
        jdbcFilmsLikesRepository.removeAllById(1L);

        //then
        Set<Long> relatedIds = jdbcFilmsLikesRepository.find(1L);
        assertThat(relatedIds).isEmpty();
    }

    @Test
    @DisplayName("Удаление всех связей")
    void removeAll_allRelationsRemovedFromDB() {
        //when
        jdbcFilmsLikesRepository.removeAll();

        //then
        Map<Long, Set<Long>> allEntities = jdbcFilmsLikesRepository.findAll();
        assertThat(allEntities).isEmpty();
    }
}
