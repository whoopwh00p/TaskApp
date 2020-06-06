package de.whoopwh00p.taskapp.persistence;

import de.whoopwh00p.taskapp.model.Task;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    Task findByName(String name);
}
