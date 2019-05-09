package de.webtech.quackr.persistence.event;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Simple CRUD repository to handle event entities.
 */
@Repository
public interface EventRepository extends CrudRepository<EventEntity, Long> {
    Collection<EventEntity> findByOrganizerId(long userId);
}
