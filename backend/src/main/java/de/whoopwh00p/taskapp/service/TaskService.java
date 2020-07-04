package de.whoopwh00p.taskapp.service;

import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.model.User;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import de.whoopwh00p.taskapp.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private TaskRepository taskRepository;
    private UserRepository userRepository;

    public TaskService(final TaskRepository taskRepository, final UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task, String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            User newUser = new User();
            newUser.setId(userId);
            user = userRepository.save(newUser);
        }
        task.setCreator(user);
        task.setReviser(user); //TODO: this needs to be sent by client
        return taskRepository.save(task);
    }
}
