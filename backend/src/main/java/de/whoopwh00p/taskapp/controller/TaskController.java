package de.whoopwh00p.taskapp.controller;

import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import io.micronaut.data.exceptions.EmptyResultException;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
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
    @Transactional
    public Iterable<Task> findAll() {
        LOGGER.debug("findAll invoked.");
        return this.taskRepository.findAll();
    }

    @Get("/findByName/{taskName}")
    public List<Task> findByName(@PathVariable String taskName) {
        LOGGER.debug("findByName invoked.");
        try {
            return Collections.singletonList(taskRepository.findByName(taskName));
        } catch (EmptyResultException e)
        {
            LOGGER.warn("Didn't find Task with Name {}", taskName, e);
            return Collections.emptyList();
        }
    }
}
