package de.whoopwh00p.taskapp.controller.dto;

import de.whoopwh00p.taskapp.model.Project;
import de.whoopwh00p.taskapp.model.TaskState;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.jcip.annotations.NotThreadSafe;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Introspected
public class TaskDto {
    @Schema(description = "The name of the task", example = "eat pizza")
    @NotEmpty
    private String name;

    @Schema(description = "the detailed description of the task", example = "the pizza has to be eaten")
    @NotEmpty
    private String description;

    @Schema(description = "the state of the task", example = "TODO")
    @Valid
    private TaskState state;
}
