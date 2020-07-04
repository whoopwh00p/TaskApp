package de.whoopwh00p.taskapp.controller.dto;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Introspected
@Getter
@Setter
public class ProjectDto {
    @NotNull
    @Schema(description = "The ID of the owner (user)", example = "1")
    private String ownerId;

    @Schema(description = "Short name of the project.", example = "MFP")
    @NotEmpty
    private String shortName;

    @Schema(description = "Full name of the project", example = "My first project")
    @NotEmpty
    private String name;
}
