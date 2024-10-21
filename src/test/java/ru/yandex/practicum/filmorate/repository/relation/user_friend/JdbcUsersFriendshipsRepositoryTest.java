package ru.yandex.practicum.filmorate.repository.relation.user_friend;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcUsersFriendshipsRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUsersFriendshipsRepositoryTest {
    private final JdbcUsersFriendshipsRepository jdbcUsersFriendshipsRepository;

    @Test
    @DisplayName("Возврат всех id сущностей, связанных с переданным id")
    void find_allRelationsByIdReturnsFromDB() {
        //when
        Set<Long> relatedIds = jdbcUsersFriendshipsRepository.find(1L);

        //then
        assertThat(relatedIds)
                .usingRecursiveComparison()
                .isEqualTo(Set.of(2L, 3L));
    }

    @Test
    @DisplayName("Возврат всех связей")
    void findAll_allRelationsReturnsFromDB() {
        //given
        Map<Long, Set<Long>> expectedEntities = Map.of(1L, Set.of(2L, 3L), 2L, Set.of(3L));

        //when
        Map<Long, Set<Long>> allFoundEntities = jdbcUsersFriendshipsRepository.findAll();

        //then
        assertThat(allFoundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Добавление одной конкретной связи")
    void insert() {
        //given
        Set<Long> expectedEntities = Set.of(1L, 3L);

        //when
        jdbcUsersFriendshipsRepository.insert(2L, 1L);

        //then
        Set<Long> foundEntities = jdbcUsersFriendshipsRepository.find(2L);
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
        jdbcUsersFriendshipsRepository.insertAny(3L, expectedEntities);

        //then
        Set<Long> foundEntities = jdbcUsersFriendshipsRepository.find(3L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Обновление всех связей сущности")
    void update() {
        //given
        Set<Long> expectedEntities = Set.of(1L, 4L);

        //when
        jdbcUsersFriendshipsRepository.update(2L, expectedEntities);

        //then
        Set<Long> foundEntities = jdbcUsersFriendshipsRepository.find(2L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Удаление одной конкретной связи")
    void remove() {
        //given
        Set<Long> expectedEntities = Set.of(3L);

        //when
        jdbcUsersFriendshipsRepository.remove(1L, 2L);

        //then
        Set<Long> foundEntities = jdbcUsersFriendshipsRepository.find(1L);
        assertThat(foundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Удаление всех связей конкретной сущности")
    void removeAllById_allRelationsByIdRemovedFromDB() {
        //when
        jdbcUsersFriendshipsRepository.removeAllById(1L);

        //then
        Set<Long> relatedIds = jdbcUsersFriendshipsRepository.find(1L);
        assertThat(relatedIds).isEmpty();
    }

    @Test
    @DisplayName("Удаление всех связей")
    void removeAll_allRelationsRemovedFromDB() {
        //when
        jdbcUsersFriendshipsRepository.removeAll();

        //then
        Map<Long, Set<Long>> allEntities = jdbcUsersFriendshipsRepository.findAll();
        assertThat(allEntities).isEmpty();
    }

    @Test
    @DisplayName("Возврат всех общих связей сущностей")
    void findCommonFriends_returnsAllCommonRelations() {
        //when
        List<Long> commonRelations = jdbcUsersFriendshipsRepository.findCommonFriends(1L, 2L);

        //then
        assertThat(commonRelations)
                .usingRecursiveComparison()
                .isEqualTo(List.of(3L));
    }
}
