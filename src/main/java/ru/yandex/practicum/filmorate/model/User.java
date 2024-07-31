package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.annotation.NoWhitespace;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User.
 */
@Data
@AllArgsConstructor
public class User {
    private final Set<Long> friends = new HashSet<>();

    @NotNull(groups = RestValidationGroups.Update.class)
    private Long id;

    @NotNull
    @Email
    @NotBlank
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
