package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.annotation.NoWhitespace;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

import java.time.LocalDate;

/**
 * User.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" })
public class User {
    @NotNull(groups = RestValidationGroups.Update.class)
    private Long id;

    @Email
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String email;

    @NotNull
    @NotBlank
    @NoWhitespace
    private String login;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;
}
