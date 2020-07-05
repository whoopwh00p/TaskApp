package de.whoopwh00p.taskapp.controller;

import de.whoopwh00p.taskapp.controller.dto.ProjectDto;
import de.whoopwh00p.taskapp.controller.dto.ProjectResponseDto;
import de.whoopwh00p.taskapp.exception.UnknownUserException;
import de.whoopwh00p.taskapp.model.Project;
import de.whoopwh00p.taskapp.persistence.ProjectRepository;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import de.whoopwh00p.taskapp.persistence.UserRepository;
import de.whoopwh00p.taskapp.service.ProjectService;
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

@Controller("/projects")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class ProjectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository, ProjectService projectService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectService = projectService;
    }

    @Get
    @Operation(summary = "gets all projects",
            description = "gets all projects")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectResponseDto.class))))
    public HttpResponse<List<ProjectResponseDto>> getProjects(Principal principal) {
        List<Project> projects = (List<Project>) projectRepository.findAll();
        return HttpResponse.ok(mapToProjectResponseDtos(projects));
    }

    @Get("/{id}")
    @Operation(summary = "get project by id",
            description = "gets a project by its id",
            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "the id of the project", required = true, example = "1"))
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Project does not exist")
    public HttpResponse<ProjectResponseDto> getProjectById(@PathVariable int id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            return HttpResponse.ok(mapToProjectResponseDto(project.get()));
        } else {
            return HttpResponse.badRequest();
        }
    }

    @Post
    @Operation(summary = "creates a new project",
            description = "creates a new project")
    @ApiResponse(responseCode = "200", description = "The created project", content = @Content(schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiResponse(responseCode = "500", description = "unexpected error occurred")
    public HttpResponse<ProjectResponseDto> createProject(@Body @Valid ProjectDto projectDto, Principal principal) {
        try {
            Project project = projectService.createProject(mapToProject(projectDto), principal.getName());
            return HttpResponse.ok(mapToProjectResponseDto(project));
        }  catch (Exception e) {
            LOGGER.error("Unexpected error occurred", e);
            return HttpResponse.serverError();
        }
    }

    @Put("/{id}")
    @Operation(summary = "updates a project",
            description = "updates a project",
            parameters = @Parameter(description = "the id of the project", example = "1"))
    @ApiResponse(responseCode = "200", description = "The updated project", content = @Content(schema = @Schema(implementation = ProjectResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Given owner-id does not exist")
    @ApiResponse(responseCode = "500", description = "unexpected error occurred")
    public HttpResponse<ProjectResponseDto> updateProject(@PathVariable int id, @Body @Valid ProjectDto projectDto) {
        try {
            Project project = mapToProject(projectDto);
            project.setId(id);
            Project updatedProject = projectRepository.update(project);
            return HttpResponse.ok(mapToProjectResponseDto(updatedProject));
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

    private Project mapToProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setShortName(projectDto.getShortName());
        return project;
    }

    private List<ProjectResponseDto> mapToProjectResponseDtos(List<Project> projects) {
        List<ProjectResponseDto> projectResponseDtos = new ArrayList<>();
        for (Project project : projects) {
            projectResponseDtos.add((mapToProjectResponseDto(project)));
        }
        return projectResponseDtos;
    }

    private ProjectResponseDto mapToProjectResponseDto(Project project) {
        if (project == null) {
            return null;
        }
        ProjectResponseDto projectResponseDto = new ProjectResponseDto();
        projectResponseDto.setId(project.getId());
        projectResponseDto.setName(project.getName());
        projectResponseDto.setShortName(project.getShortName());
        return projectResponseDto;
    }
}
