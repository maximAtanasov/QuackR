package de.webtech.quackr.persistence.event;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Simple CRUD repository to handle event entities.
 */
@Repository
public interface EventRepository extends CrudRepository<EventEntity, Long> {

    /**
     * Finds all events organized by the user with the given id.
     * @param userId The id of the user.
     * @return All events organized by the user.
     */
    Collection<EventEntity> findByOrganizerId(long userId);
}
