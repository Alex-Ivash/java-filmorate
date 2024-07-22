package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userController, "users", new HashMap<Integer, User>());
        ReflectionTestUtils.setField(userController, "seq", 0);
    }

    @Test
    @DisplayName("На GET запрос при отсутствии пользователей возвращается пустой массив")
    void getUsers_ReturnsEmptyJSONArray_NoUsersExist() throws Exception {
        mvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("На GET запрос при наличии пользователей возвращается массив пользователей")
    void getUsers_ReturnsJSONArray_UsersExist() throws Exception {
        Map<Integer, User> userMap = new HashMap<>() {{
            put(1, new User(1, "mail1@mail.ru", "dolor1e", "Nick Nam1e", LocalDate.of(1946, 8, 20)));
            put(2, new User(1, "mail2@mail.ru", "dolore2", "Nick Name2", LocalDate.of(1946, 8, 21)));
        }};

        ReflectionTestUtils.setField(userController, "users", userMap);

        mvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("На POST запрос c правильным телом создается новый пользователь")
    void createUser_CreatedUser_ValidRequestBody() throws Exception {
        String requestBody = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody))
                .andExpect(jsonPath("$.id").value(1));

        var users = ((HashMap<Integer, Film>)ReflectionTestUtils.getField(userController, "users"));
        assertEquals(1, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c пустым телом не создается новый пользователь и возвращается ошибка 400")
    void createUser_UserNotCreated_EmptyRequestBody() throws Exception {
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(0, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c отсутствующим email не создается новый пользователь и возвращается ошибка 400")
    void createUser_UserNotCreated_NullEmailInRequestBody() throws Exception {
        String requestBody = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(0, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c пустым email не создается новый пользователь и возвращается ошибка 400")
    void createUser_UserNotCreated_EmptyEmailInRequestBody() throws Exception {
        String requestBody = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(0, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c невалидным email не создается новый пользователь и возвращается ошибка 400")
    void createUser_UserNotCreated_InvalidEmailInRequestBody() throws Exception {
        String requestBody = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"это-неправильный?эмейл@\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(0, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c отсутствующим login не создается новый пользователь и возвращается ошибка 400")
    void createUser_UserNotCreated_NullLoginInRequestBody() throws Exception {
        String requestBody = "{\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(0, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c пустым login не создается новый пользователь и возвращается ошибка 400")
    void createUser_UserNotCreated_EmptyLoginInRequestBody() throws Exception {
        String requestBody = "{\"login\":\"\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(0, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c пробелами в login не создается новый пользователь и возвращается ошибка 400")
    void createUser_UserNotCreated_LoginIncludesSpaces() throws Exception {
        String requestBody = "{\"login\":\"dol ore \",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(0, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c отсутствующим name создается новый пользователь c login в качестве name")
    void createUser_UserCreated_NullNameInRequestBody() throws Exception {
        String requestBody = "{\"login\":\"dolore\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("dolore"));

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(1, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c пустым name создается новый пользователь c login в качестве name")
    void createUser_UserCreated_EmptyNameInRequestBody() throws Exception {
        String requestBody = "{\"login\":\"dolore\",\"name\":\"\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("dolore"));

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(1, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c отсутствующей birthday не создается новый пользователь и возвращается ошибка 400")
    void createUser_UserNotCreated_NullBirthdayInRequestBody() throws Exception {
        String requestBody = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\"}";

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(0, users.values().size());
    }

    @Test
    @DisplayName("На POST запрос c birthday в будущем не создается новый пользователь и возвращается ошибка 400")
    void createUser_UserNotCreated_EmptyBirthdayInRequestBody() throws Exception {
        String requestBody = String.format("{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"%s\"}", LocalDate.now().plusDays(1));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        var users = (Map<Integer, User>) ReflectionTestUtils.getField(userController, "users");

        assertEquals(0, users.values().size());
    }

    @Test
    @DisplayName("На PUT запрос с пустым телом возвращается ошибка 400")
    void updateUser_UserNotUpdated_RequestBodyIsEmpty() throws Exception {
        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("На PUT запрос с не заданным id в теле возвращается ошибка 400")
    void updateUser_UserNotUpdated_IdInRequestBodyIsEmpty() throws Exception {
        String requestBody = "{\"login\":\"dolore\",\"name\":\"Nick Name\",\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\"}";

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("На PUT запрос с не существующим id в теле возвращается ошибка 404")
    void updateFilm_UserNotUpdated_IdInRequestBodyIsNotExist() throws Exception {
        String requestBody = "{\"id\":999,\"login\":\"doloreMike\",\"name\":\"Nick Name hello\",\"email\":\"foo@bar.ru\",\"birthday\":\"1946-08-23\"}";

        Map<Integer, User> userMap = new HashMap<>() {{
            put(1, new User(1, "mail1@mail.ru", "dolor1e", "Nick Nam1e", LocalDate.of(1946, 8, 20)));
            put(2, new User(1, "mail2@mail.ru", "dolore2", "Nick Name2", LocalDate.of(1946, 8, 21)));
        }};

        ReflectionTestUtils.setField(userController, "users", userMap);

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("На PUT запрос с корректным id в теле обновляется пользователь с заданным id")
    void updateFilm_UserUpdated_IdInRequestBodyIsValid() throws Exception {
        String requestBody = "{\"id\":1,\"login\":\"doloreMike\",\"name\":\"Nick Name hello\",\"email\":\"foo@bar.ru\",\"birthday\":\"1946-08-23\"}";
        User expectedUserState = new User(1, "foo@bar.ru", "doloreMike", "Nick Name hello", LocalDate.of(1946, 8, 23));

        Map<Integer, User> userMap = new HashMap<>() {{
            put(1, new User(1, "mail1@mail.ru", "dolor1e", "Nick Nam1e", LocalDate.of(1946, 8, 20)));
            put(2, new User(2, "mail2@mail.ru", "dolore2", "Nick Name2", LocalDate.of(1946, 8, 21)));
        }};

        ReflectionTestUtils.setField(userController, "users", userMap);

        var users = ((HashMap<Integer, User>)ReflectionTestUtils.getField(userController, "users"));

        mvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        assertAll(
                () -> assertEquals(expectedUserState, users.get(1)),
                () -> assertEquals(userMap.get(2), users.get(2))
        );
    }
}
