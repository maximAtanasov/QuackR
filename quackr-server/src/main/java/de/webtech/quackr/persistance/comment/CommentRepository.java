package de.webtech.quackr.persistance.comment;

import de.webtech.quackr.persistance.event.EventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Simple CRUD repository to handle event entities.
 */
@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    Collection<CommentEntity> findByEventId(long eventId);
}
