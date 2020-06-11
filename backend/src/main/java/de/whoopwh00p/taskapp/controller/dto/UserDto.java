package de.whoopwh00p.taskapp.controller.dto;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Introspected
public class UserDto {
    @ArraySchema(schema = @Schema(example = "1"))
    private Set<Integer> projectIds;

    @Schema(description = "the name of the user", example = "John Doe")
    @NotEmpty
    private String name;
}
