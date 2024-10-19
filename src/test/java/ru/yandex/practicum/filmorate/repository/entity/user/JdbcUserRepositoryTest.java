package ru.yandex.practicum.filmorate.repository.entity.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcUserRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUserRepositoryTest {
    private final JdbcUserRepository jdbcUserRepository;

    static User getTestEntity1() {
        return new User(
                1L,
                "foo@bar.com",
                "fooBar",
                "Alex",
                LocalDate.of(1998, 4, 23)
        );
    }

    static User getTestEntity2() {
        return new User(
                2L,
                "he@eh.ee",
                "hee",
                "hhe",
                LocalDate.of(1966, 11, 2)
        );
    }

    static User getTestEntity3() {
        return new User(
                3L,
                "john.doe@example.com",
                "johndoe",
                "John Doe",
                LocalDate.of(1990, 1, 1)
        );
    }

    static User getTestEntity4() {
        return new User(
                4L,
                "tony.stark@avengers.com",
                "ironman",
                "Tony Stark",
                LocalDate.of(1970, 5, 29)
        );
    }

    static User getTestEntityWithNoEquivalentInDB() {
        return new User(
                null,
                "foo@pop.com",
                "foo",
                "Jack",
                LocalDate.of(1992, 1, 12)
        );
    }

    @Test
    @DisplayName("Создание новой сущности")
    void create_entityCreatedInDB() {
        //given
        User expectedEntity = getTestEntityWithNoEquivalentInDB();
        expectedEntity.setId(4L);

        //when
        jdbcUserRepository.create(getTestEntityWithNoEquivalentInDB());

        //then
        Optional<User> createdEntity = jdbcUserRepository.find(5L);
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
        User changedEntity = getTestEntityWithNoEquivalentInDB();
        changedEntity.setId(1L);

        //when
        jdbcUserRepository.update(changedEntity);

        //then
        Optional<User> updatedEntity = jdbcUserRepository.find(1L);
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
        Optional<User> entity = jdbcUserRepository.find(1L);

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
        List<User> expectedEntities = List.of(
                getTestEntity1(),
                getTestEntity2(),
                getTestEntity3(),
                getTestEntity4());

        //when
        List<User> allFoundEntities = jdbcUserRepository.findAll();

        //then
        assertThat(allFoundEntities)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntities);
    }

    @Test
    @DisplayName("Удаление сущности по id")
    void remove_entityRemovedFromDB() {
        //when
        jdbcUserRepository.remove(1L);

        //then
        Optional<User> entity = jdbcUserRepository.find(1L);
        assertThat(entity).isNotPresent();
    }

    @Test
    @DisplayName("Удаление всех сущностей")
    void removeAll_allEntityRemoverFromDB() {
        //when
        jdbcUserRepository.removeAll();

        //then
        List<User> allEntities = jdbcUserRepository.findAll();
        assertThat(allEntities).isEmpty();
    }
}
