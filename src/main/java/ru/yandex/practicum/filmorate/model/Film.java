package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.annotation.DateNotBefore;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Film {
    @NotNull(groups = RestValidationGroups.Update.class)
    private Long id;

    private Set<Genre> genres;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    @DateNotBefore
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private Integer duration;

    private Mpa mpa;
}
