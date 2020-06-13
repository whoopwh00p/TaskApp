package de.whoopwh00p.taskapp.controller.dto;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = "projectIds")
@Introspected
public class UserDto {
    @ArraySchema(schema = @Schema(example = "1"))
    private Set<Integer> projectIds;

    @Schema(description = "the name of the user", example = "John Doe")
    @NotEmpty
    private String name;
}
