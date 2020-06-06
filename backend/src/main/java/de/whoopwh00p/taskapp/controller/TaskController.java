package de.whoopwh00p.taskapp.controller;

import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository)
    {
        this.taskRepository = taskRepository;
    }

    @Get("/findAll")
    public Iterable<Task> findAll() {
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
