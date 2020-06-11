package de.whoopwh00p.taskapp.persistence;

import de.whoopwh00p.taskapp.model.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
