package de.webtech.quackr.persistance.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Long, User> {
}
