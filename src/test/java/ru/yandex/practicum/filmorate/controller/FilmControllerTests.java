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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
public class FilmControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(filmController, "films", new HashMap<Integer, Film>());
        ReflectionTestUtils.setField(filmController, "seq", 0);
    }

    @Test
    @DisplayName("На GET запрос при отсутствии фильмов возвращается пустой массив")
    void getFilms_ReturnsEmptyJSONArray_NoMoviesExist() throws Exception {
        mvc.perform(get("/films").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("На GET запрос при наличии фильмов возвращается массив фильмов")
    void getFilms_ReturnsJSONArray_MoviesExist() throws Exception {
        Map<Integer, Film> filmMap = new HashMap<>() {{
            put(1, new Film(1, "name", "description", LocalDate.of(1967, 3, 25), 125));
            put(2, new Film(2, "name", "description", LocalDate.of(1967, 3, 25), 125));
        }};

        ReflectionTestUtils.setField(filmController, "films", filmMap);

        mvc.perform(get("/films").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("На POST запрос c правильным телом создается новый фильм")
    void createFilm_CreatedFilm_ValidRequestBody() throws Exception {
        String requestBody = "{\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\",\"duration\":100}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody))
                .andExpect(jsonPath("$.id").value(1));

        var films = ((HashMap<Integer, Film>)ReflectionTestUtils.getField(filmController, "films"));
        assertEquals(1, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос c пустым телом не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_EmptyRequestBody() throws Exception {
        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос c отсутствующим name не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_NullNameInRequestBody() throws Exception {
        String requestBody = "{\"description\":\"A thrilling movie\",\"releaseDate\":\"2024-07-20\",\"duration\":120}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос c пустым name не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_EmptyNameInRequestBody() throws Exception {
        String requestBody = "{\"name\":\"\",\"description\":\"A thrilling movie\",\"releaseDate\":\"2024-07-20\",\"duration\":120}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос c отсутствующим description не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_NullDescriptionInRequestBody() throws Exception {
        String requestBody = "{\"name\":\"Thriller\",\"releaseDate\":\"2024-07-20\",\"duration\":120}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос с description превышающим 200 символов не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_DescriptionTooLong() throws Exception {
        String longDescription = "a".repeat(201);
        String requestBody = String.format("{\"name\":\"Thriller\",\"description\":\"%s\",\"releaseDate\":\"2024-07-20\",\"duration\":120}", longDescription);

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос c отсутствующей releaseDate не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_NullReleaseDateInRequestBody() throws Exception {
        String requestBody = "{\"name\":\"Thriller\",\"description\":\"A thrilling movie\",\"duration\":120}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос с датой релиза раньше 28 декабря 1895 года не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_ReleaseDateBeforeLowerBound() throws Exception {
        String requestBody = "{\"name\":\"Old Film\",\"description\":\"An old movie\",\"releaseDate\":\"1895-12-27\",\"duration\":120}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос с датой релиза точно 28 декабря 1895 года создается новый фильм и возвращается статус 200")
    void createFilm_FilmCreated_ReleaseDateEqualToLowerBound() throws Exception {
        String requestBody = "{\"name\":\"Historic Film\",\"description\":\"A historic movie\",\"releaseDate\":\"1895-12-28\",\"duration\":120}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(1, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос с датой релиза после 28 декабря 1895 года создается новый фильм и возвращается статус 200")
    void createFilm_FilmCreated_ReleaseDateAfterLowerBound() throws Exception {
        String requestBody = "{\"name\":\"Modern Film\",\"description\":\"A modern movie\",\"releaseDate\":\"2024-07-20\",\"duration\":120}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(1, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос c отсутствующей duration не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_NullDurationInRequestBody() throws Exception {
        String requestBody = "{\"name\":\"Thriller\",\"description\":\"A thrilling movie\",\"releaseDate\":\"2024-07-20\"}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос с отрицательной длительностью фильма не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_NegativeDurationInRequestBody() throws Exception {
        String requestBody = "{\"name\":\"Thriller\",\"description\":\"A thrilling movie\",\"releaseDate\":\"2024-07-20\",\"duration\":-120}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На POST запрос с не валидным JSON не создается новый фильм и возвращается ошибка 400")
    void createFilm_FilmNotCreated_InvalidJSONInRequestBody() throws Exception {
        String requestBody = "{\"name\":\"Thriller\"\"description\":\"A thrilling movie,\"releaseDate\":\"2024-07-20\",\"duration\":-120}";

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Map<Integer, Film> films = (Map<Integer, Film>) ReflectionTestUtils.getField(filmController, "films");

        assertEquals(0, films.values().size());
    }

    @Test
    @DisplayName("На PUT запрос с пустым телом возвращается ошибка 400")
    void updateFilm_FilmNotUpdated_RequestBodyIsEmpty() throws Exception {
        mvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("На PUT запрос с не заданным id в теле возвращается ошибка 400")
    void updateFilm_FilmNotUpdated_IdInRequestBodyIsEmpty() throws Exception {
        String requestBody = "{\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\",\"duration\":100}";

        mvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("На PUT запрос с не существующим id в теле возвращается ошибка 404")
    void updateFilm_FilmNotUpdated_IdInRequestBodyIsNotExist() throws Exception {
        String requestBody = "{\"id\":999,\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\",\"duration\":100}";

        Map<Integer, Film> filmMap = new HashMap<>() {{
            put(1, new Film(1, "name", "description", LocalDate.of(1967, 3, 25), 125));
            put(2, new Film(2, "name", "description", LocalDate.of(1967, 3, 25), 125));
        }};

        ReflectionTestUtils.setField(filmController, "films", filmMap);

        mvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("На PUT запрос с корректным id в теле обновляется фильм с заданным id")
    void updateFilm_FilmUpdated_IdInRequestBodyIsValid() throws Exception {
        String requestBody = "{\"id\":1,\"name\":\"newName\",\"description\":\"newDescription\",\"releaseDate\":\"1967-03-26\",\"duration\":200}";
        Film expectedFilmState = new Film(1, "newName", "newDescription", LocalDate.of(1967, 3, 26), 200);

        Map<Integer, Film> filmMap = new HashMap<>() {{
            put(1, new Film(1, "name", "description", LocalDate.of(1967, 3, 25), 125));
            put(2, new Film(2, "name", "description", LocalDate.of(1967, 3, 25), 125));
        }};

        ReflectionTestUtils.setField(filmController, "films", filmMap);
        var films = ((HashMap<Integer, Film>)ReflectionTestUtils.getField(filmController, "films"));

        mvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        assertAll(
                () -> assertEquals(expectedFilmState, films.get(1)),
                () -> assertEquals(filmMap.get(2), films.get(2))
        );
    }
}
