package de.whoopwh00p.taskapp.controller;

import de.whoopwh00p.taskapp.controller.dto.TaskDto;
import de.whoopwh00p.taskapp.exception.UnknownProjectException;
import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.persistence.ProjectRepository;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
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
import java.util.List;
import java.util.Optional;

@Controller("/tasks")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public TaskController(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Get
    @Operation(summary = "gets all tasks",
            description = "gets all tasks")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class))))
    public HttpResponse<List<Task>> getTasks() {
        return HttpResponse.ok((List<Task>) taskRepository.findAll());
    }

    @Get("/{id}")
    @Operation(summary = "get task by id",
            description = "gets a task by its id",
            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "the id of the task", required = true, example = "1"))
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Task.class)))
    @ApiResponse(responseCode = "400", description = "Task does not exist")
    public HttpResponse<Task> getTaskById(@PathVariable int id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return HttpResponse.ok(task.get());
        } else {
            return HttpResponse.badRequest();
        }
    }

    @Post
    @Operation(summary = "creates a new task",
            description = "creates a new task")
    @ApiResponse(responseCode = "200", description = "The created task", content = @Content(schema = @Schema(implementation = Task.class)))
    @ApiResponse(responseCode = "400", description = "Given project-id does not exist")
    public HttpResponse<Task> createTask(@Body @Valid TaskDto taskDto) {
        try {
            Task task = taskRepository.save(mapToTask(taskDto));
            return HttpResponse.ok(task);
        } catch (Exception e) {
            LOGGER.warn("Could not save task", e);
            return HttpResponse.badRequest();
        }
    }

    @Delete("/{id}")
    @Operation(summary = "deletes a task",
            description = "deletes the task with the given id",
            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "the id of the task", required = true, example = "1"))
    @ApiResponse(responseCode = "200", description = "task was deleted")
    @ApiResponse(responseCode = "500", description = "unexpected error occurred")
    public HttpResponse<String> deleteTask(@PathVariable Integer id) {
        try {
            taskRepository.deleteById(id);
            return HttpResponse.ok();
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred", e);
            return HttpResponse.serverError();
        }
    }

    private Task mapToTask(TaskDto taskDto) throws UnknownProjectException {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setProject(projectRepository.findById(taskDto.getProjectId()).orElseThrow(() -> new UnknownProjectException("No project with this id exists")));
        return task;
    }
}
