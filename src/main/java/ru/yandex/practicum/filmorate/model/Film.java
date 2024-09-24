package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.annotation.NotBefore;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@AllArgsConstructor
public class Film {
    private final Set<Long> likes = new HashSet<>();

    @NotNull(groups = RestValidationGroups.Update.class)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    @NotBefore
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private Integer duration;
}
