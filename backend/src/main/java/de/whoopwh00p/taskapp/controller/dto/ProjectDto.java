package de.whoopwh00p.taskapp.controller.dto;

import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.model.User;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Introspected
public class ProjectDto {
    @Schema(implementation = Task.class, description = "the tasks that belong to the project")
    private Set<Task> tasks;

    @Schema(implementation = User.class, description = "The users that belong to the project")
    private Set<User> users;


    @NotNull
    @Schema(name = "ownerId", description = "The ID of the owner (user)", example = "1")
    private int ownerId;

    @Schema(name = "shortName", description = "Short name of the project.", example = "MFP")
    @NotEmpty
    private String shortName;

    @Schema(description = "Full name of the project", example = "My first project")
    @NotEmpty
    private String name;
}
