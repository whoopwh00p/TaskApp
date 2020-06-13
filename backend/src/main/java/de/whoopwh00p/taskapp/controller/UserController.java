package de.whoopwh00p.taskapp.controller;

import de.whoopwh00p.taskapp.controller.dto.UserDto;
import de.whoopwh00p.taskapp.controller.dto.UserResponseDto;
import de.whoopwh00p.taskapp.model.Project;
import de.whoopwh00p.taskapp.model.User;
import de.whoopwh00p.taskapp.persistence.ProjectRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public UserController(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Get
    @Operation(summary = "gets all users",
            description = "gets all users")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))))
    public HttpResponse<List<UserResponseDto>> getUsers() {
        return HttpResponse.ok(mapToUserResponseDtos((List<User>) userRepository.findAll()));
    }

    @Get("/{id}")
    @Operation(summary = "get user by id",
            description = "gets a user by its id",
            parameters = @Parameter(in = ParameterIn.PATH, name = "id", description = "the id of the user", required = true, example = "1"))
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "User does not exist")
    public HttpResponse<UserResponseDto> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return HttpResponse.ok(mapToUserResponseDto(user.get()));
        } else {
            return HttpResponse.notFound();
        }
    }

    @Post
    @Operation(summary = "creates a new user",
            description = "creates a new user")
    @ApiResponse(responseCode = "200", description = "The created user", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Given owner-id does not exist")
    public HttpResponse<UserResponseDto> createUser(@Body @Valid UserDto userDto) {
        try {
            User user = userRepository.save(mapToUser(userDto));
            return HttpResponse.ok(mapToUserResponseDto(user));
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred", e);
            return HttpResponse.serverError();
        }
    }

    @Put("/{id}")
    @Operation(summary = "updates a user",
            description = "updates a user",
            parameters = @Parameter(description = "the id of the user", example = "1"))
    @ApiResponse(responseCode = "200", description = "The updated user", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Given owner-id does not exist")
    public HttpResponse<UserResponseDto> updateUser(@PathVariable int id, @Body @Valid UserDto userDto) {
        try {
            User user = mapToUser(userDto);
            user.setId(id);
            User updatedUser = userRepository.update(user);
            return HttpResponse.ok(mapToUserResponseDto(updatedUser));
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred", e);
            return HttpResponse.serverError();
        }
    }

    private User mapToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        if (userDto.getProjectIds() != null) {
            List<Project> projects = new ArrayList<>();
            for (Integer id : userDto.getProjectIds()) {
                projectRepository.findById(id).ifPresent(projects::add);
            }
            user.setProjects(projects);
        }
        return user;
    }

    private List<UserResponseDto> mapToUserResponseDtos(List<User> users) {
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User user : users) {
            userResponseDtos.add(mapToUserResponseDto(user));
        }
        return userResponseDtos;
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setProjectIds(user.getProjects().stream().map(Project::getId).collect(Collectors.toList()));
        return userResponseDto;
    }
}