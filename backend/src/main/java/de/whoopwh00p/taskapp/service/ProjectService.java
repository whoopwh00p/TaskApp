package de.whoopwh00p.taskapp.service;

import de.whoopwh00p.taskapp.model.Project;
import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.model.User;
import de.whoopwh00p.taskapp.persistence.ProjectRepository;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import de.whoopwh00p.taskapp.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    public ProjectService(final ProjectRepository projectRepository, final UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project createProject(Project project, String creatorId) {
        project.setCreator(findUserById(creatorId));
        return projectRepository.save(project);
    }


    private User findUserById(String id) {
        if(id == null || id.isEmpty()) {
            return null;
        }
        Optional<User> optionalUser = userRepository.findById(id);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            User newUser = new User();
            newUser.setId(id);
            user = userRepository.save(newUser);
        }
        return user;
    }

}
