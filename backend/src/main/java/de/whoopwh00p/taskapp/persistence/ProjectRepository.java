package de.whoopwh00p.taskapp.persistence;

import de.whoopwh00p.taskapp.model.Project;
import de.whoopwh00p.taskapp.model.Task;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer> {
}
