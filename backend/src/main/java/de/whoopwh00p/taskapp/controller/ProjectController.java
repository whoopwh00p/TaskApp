package de.whoopwh00p.taskapp.controller;

import de.whoopwh00p.taskapp.controller.dto.ProjectDto;
import de.whoopwh00p.taskapp.exception.UnknownUserException;
import de.whoopwh00p.taskapp.model.Project;
import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.model.User;
import de.whoopwh00p.taskapp.persistence.ProjectRepository;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import de.whoopwh00p.taskapp.persistence.UserRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller("/project")
public class ProjectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Get
    @Operation(summary = "gets all projects",
            description = "gets all projects")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Project.class))))
    public HttpResponse<List<Project>> getProjects() {
        return HttpResponse.ok((List<Project>) projectRepository.findAll());
    }

    @Get("/{id}")
    @Operation(summary = "get project by id",
            description = "gets a project by its id",
            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "the id of the project", required = true, example = "1"))
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Project.class)))
    @ApiResponse(responseCode = "400", description = "Project does not exist")
    public HttpResponse<Project> getProjectById(@PathVariable int id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            return HttpResponse.ok(project.get());
        } else {
            return HttpResponse.badRequest();
        }
    }

    @Post
    @Operation(summary = "creates a new project",
            description = "creates a new project")
    @ApiResponse(responseCode = "200", description = "The created project", content = @Content(schema = @Schema(implementation = Project.class)))
    @ApiResponse(responseCode = "400", description = "Given owner-id does not exist")
    @ApiResponse(responseCode = "500", description = "unexpected error occurred")
    public HttpResponse<Project> createProject(@Body @Valid ProjectDto projectDto) {
        try {
            Project project = projectRepository.save(mapToProject(projectDto));
            return HttpResponse.ok(project);
        } catch (UnknownUserException e) {
            LOGGER.warn("Could not save project", e);
            return HttpResponse.badRequest();
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred", e);
            return HttpResponse.serverError();
        }
    }

    @Delete("/{id}")
    @Operation(summary = "deletes a project",
            description = "deletes the project with the given id",
            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "the id of the project", required = true, example = "1"))
    @ApiResponse(responseCode = "200", description = "project was deleted")
    @ApiResponse(responseCode = "500", description = "unexpected error occurred")
    public HttpResponse<String> deleteProject(@PathVariable Integer id) {
        try {
            projectRepository.deleteById(id);
            return HttpResponse.ok();
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred", e);
            return HttpResponse.serverError();
        }
    }

    private Project mapToProject(ProjectDto projectDto) throws UnknownUserException {
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setShortName(projectDto.getShortName());
        if (projectDto.getUserIds() != null) {
            Set<User> users = new HashSet<>();
            for (Integer id : projectDto.getUserIds()) {
                userRepository.findById(id).ifPresent(users::add);
            }
            project.setUsers(users);
        }
        if (projectDto.getTaskIds() != null) {
            Set<Task> tasks = new HashSet<>();
            for (Integer id : projectDto.getTaskIds()) {
                taskRepository.findById(id).ifPresent(tasks::add);
            }
            project.setTasks(tasks);
        }
        project.setOwner(userRepository.findById(projectDto.getOwnerId()).orElseThrow(() -> new UnknownUserException("No user with this id exists")));
        return project;
    }
}
