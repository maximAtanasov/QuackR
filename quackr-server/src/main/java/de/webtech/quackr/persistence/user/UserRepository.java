package de.webtech.quackr.persistence.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Simple CRUD repository for handling users.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    /**
     * Tells whether a user exists given a username.
     * @param username The username to check for.
     * @return True if the user exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Finds a user given their username.
     * @param username The username to look for.
     * @return The user with the given username, or null.
     */
    UserEntity findByUsername(String username);

    /**
     * Finds all users with the given tole.
     * @param role The role to look for.
     * @return All users with the given role.
     */
    Collection<UserEntity> findByRole(UserRole role);
}
