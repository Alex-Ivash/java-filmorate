package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.annotation.group.RestValidationGroups;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = { "id" })
public class Mpa {
    @NotNull(groups = RestValidationGroups.Update.class)
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name;
}
