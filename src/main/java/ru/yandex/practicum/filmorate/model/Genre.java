package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

@AllArgsConstructor
@Data
@EqualsAndHashCode(of = { "id" })
public class Genre {
    @NotNull(groups = RestValidationGroups.Update.class)
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 200)
    private String name;
}
