package de.whoopwh00p.taskapp.controller.dto;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = "projectIds")
@Introspected
public class UserResponseDto {
    @NotNull
    @Schema(description = "Unique ID of the user.", example = "1")
    private String id;

    @Schema(description = "The projects that the user belongs to")
    @ArraySchema(schema = @Schema(example = "1"))
    private List<Integer> projectIds;

    @Schema(description = "the name of the user", example = "John Doe")
    @NotEmpty
    private String name;
}
