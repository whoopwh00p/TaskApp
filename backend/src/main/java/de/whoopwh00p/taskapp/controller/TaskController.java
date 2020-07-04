package de.whoopwh00p.taskapp.controller;

import de.whoopwh00p.taskapp.controller.dto.TaskDto;
import de.whoopwh00p.taskapp.controller.dto.TaskResponseDto;
import de.whoopwh00p.taskapp.exception.UnknownProjectException;
import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.persistence.ProjectRepository;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import de.whoopwh00p.taskapp.service.TaskService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller("/projects/{projectId}/tasks")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public TaskController(ProjectRepository projectRepository, TaskRepository taskRepository, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @Get
    @Operation(summary = "gets all tasks",
            description = "gets all tasks for a specific project",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "projectId", description = "the id of the project", required = true, example = "1"),
            })
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponseDto.class))))
    public HttpResponse<List<TaskResponseDto>> getTasks(@PathVariable int projectId) {
        return HttpResponse.ok(mapToTaskResponseDtos(taskRepository.findByProjectId(projectId)));
    }

    @Get("/{id}")
    @Operation(summary = "get task by id",
            description = "gets a task by its id",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "projectId", description = "the id of the project", required = true, example = "1"),
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "the id of the task", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TaskResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Task or project does not exist")
    public HttpResponse<TaskResponseDto> getTaskById(@PathVariable int projectId, @PathVariable int id) {
        Optional<Task> task = taskRepository.findByProjectIdAndId(projectId, id);
        if (task.isPresent()) {
            return HttpResponse.ok(mapToTaskResponseDto(task.get()));
        } else {
            return HttpResponse.notFound();
        }
    }

    @Post
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "creates a new task",
            description = "creates a new task",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "projectId", description = "the id of the project", required = true, example = "1"),
            })
    @ApiResponse(responseCode = "200", description = "The created task", content = @Content(schema = @Schema(implementation = TaskResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Given project-id does not exist")
    public HttpResponse<TaskResponseDto> createTask(@PathVariable int projectId, @Body @Valid TaskDto taskDto, Principal principal) {
        try {
            LOGGER.info("createTask called by {}", principal.getName());
            Task task = taskService.createTask(mapToTask(taskDto, projectId), principal.getName(), taskDto.getAssigneeId());
            return HttpResponse.ok(mapToTaskResponseDto(task));
        } catch (Exception e) {
            LOGGER.warn("Could not save task", e);
            return HttpResponse.badRequest();
        }
    }

    @Put("/{id}")
    @Operation(summary = "updates a task",
            description = "updates a task",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "projectId", description = "the id of the project. " +
                            "If this id is not equal to the original task-id, the task will be transferred to the new project", required = true, example = "1"),
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "the id of the task", required = true, example = "1"),
            })
    @ApiResponse(responseCode = "200", description = "The updated task", content = @Content(schema = @Schema(implementation = TaskResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Given project-id does not exist")
    public HttpResponse<TaskResponseDto> updateTask(@PathVariable int projectId, @PathVariable int id, @Body @Valid TaskDto taskDto, Principal principal) {
        try {
            Task task = mapToTask(taskDto, projectId);
            task.setId(id);
            Task updatedTask = taskService.updateTask(task, taskDto.getAssigneeId());
            return HttpResponse.ok(mapToTaskResponseDto(updatedTask));
        } catch (Exception e) {
            LOGGER.warn("Could not save task", e);
            return HttpResponse.badRequest();
        }
    }

    @Delete("/{id}")
    @Operation(summary = "deletes a task",
            description = "deletes the task with the given id",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "projectId", description = "the id of the project", required = true, example = "1"),
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "the id of the task", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", description = "task was deleted")
    @ApiResponse(responseCode = "500", description = "unexpected error occurred")
    public HttpResponse<String> deleteTask(@PathVariable int projectId, @PathVariable Integer id) {
        try {
            taskRepository.deleteByProjectIdAndId(projectId, id);
            return HttpResponse.ok();
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred", e);
            return HttpResponse.serverError();
        }
    }

    private Task mapToTask(TaskDto taskDto, int projectId) throws UnknownProjectException {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setProject(projectRepository.findById(projectId).orElseThrow(() -> new UnknownProjectException("No project with this id exists")));
        task.setDescription(taskDto.getDescription());
        task.setState(taskDto.getState());
        return task;
    }

    private List<TaskResponseDto> mapToTaskResponseDtos(List<Task> tasks) {
        List<TaskResponseDto> taskResponseDtos = new ArrayList<>();
        for (Task task : tasks) {
            taskResponseDtos.add((mapToTaskResponseDto(task)));
        }
        return taskResponseDtos;
    }

    private TaskResponseDto mapToTaskResponseDto(Task task) {
        if (task == null) {
            return null;
        }
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(task.getId());
        taskResponseDto.setDescription(task.getDescription());
        taskResponseDto.setName(task.getName());
        taskResponseDto.setProjectId(task.getProject().getId());
        taskResponseDto.setState(task.getState());
        if(task.getAssignee() != null)
        {
            taskResponseDto.setAssigneeId(task.getAssignee().getId());
            taskResponseDto.setAssigneeName(task.getAssignee().getName());
        }
        return taskResponseDto;
    }
}
