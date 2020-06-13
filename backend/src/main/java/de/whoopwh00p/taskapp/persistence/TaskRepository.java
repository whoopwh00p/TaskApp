package de.whoopwh00p.taskapp.persistence;

import de.whoopwh00p.taskapp.model.Task;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findByProjectId(int projectId);
    Optional<Task> findByProjectIdAndId(int projectId, int id);

    @Query("DELETE FROM Task WHERE project_id = :projectId AND id = :id")
    void deleteByProjectIdAndId(int projectId, int id);
}
