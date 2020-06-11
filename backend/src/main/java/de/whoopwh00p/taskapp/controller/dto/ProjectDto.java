package de.whoopwh00p.taskapp.controller.dto;

import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.model.User;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Introspected
public class ProjectDto {
    @ArraySchema(schema = @Schema(description = "the tasks that belong to the project", example = "1"))
    private Set<Integer> taskIds;

    @ArraySchema(schema = @Schema(description = "The users that belong to the project",example = "1"))
    private Set<Integer> userIds;

    @NotNull
    @Schema(description = "The ID of the owner (user)", example = "1")
    private int ownerId;

    @Schema(description = "Short name of the project.", example = "MFP")
    @NotEmpty
    private String shortName;

    @Schema(description = "Full name of the project", example = "My first project")
    @NotEmpty
    private String name;
}
