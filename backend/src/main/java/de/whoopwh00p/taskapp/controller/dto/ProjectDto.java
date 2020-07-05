package de.whoopwh00p.taskapp.controller.dto;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Introspected
@Getter
@Setter
public class ProjectDto {
    @Schema(description = "Short name of the project.", example = "MFP")
    @NotEmpty
    private String shortName;

    @Schema(description = "Full name of the project", example = "My first project")
    @NotEmpty
    private String name;
}
