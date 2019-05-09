package de.webtech.quackr.persistence.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Simple CRUD repository for handling users.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);
}
