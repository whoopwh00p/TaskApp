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

    public Task createTask(Task task, String creatorId, String assigneeId) {
        task.setCreator(findUserById(creatorId));
        task.setAssignee(findUserById(assigneeId));
        return taskRepository.save(task);
    }

    public Task updateTask(Task task, String assigneeId) {
        task.setAssignee(findUserById(assigneeId));
        task.setCreator(taskRepository.findById(task.getId()).map(Task::getCreator).orElse(null));
        return taskRepository.update(task);
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
