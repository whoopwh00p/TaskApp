package de.whoopwh00p.taskapp.controller;

import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/tasks")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository)
    {
        this.taskRepository = taskRepository;
    }

    @Get("/findAll")
    public Iterable<Task> findAll() {
        LOGGER.info("findAll invoked.");
        return this.taskRepository.findAll();
    }

    @Get("/addTask")
    public Task addTask() {
        Task task = new Task();
        task.setName("TaskName");
        task.setCompleted(true);
        return taskRepository.save(task);
    }
}
