package de.whoopwh00p.taskapp.controller;

import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

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

    @Get("/getByName/{taskName}")
    public List<Task> getByName(@PathVariable String taskName) {
        try {
            return Collections.singletonList(taskRepository.findByName(taskName));
        } catch (Exception e)
        {
            LOGGER.warn("Didn't find Task with Name {}", taskName, e);
            return Collections.emptyList();
        }
    }
}
