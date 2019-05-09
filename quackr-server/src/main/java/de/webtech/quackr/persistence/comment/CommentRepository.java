package de.webtech.quackr.persistence.comment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Simple CRUD repository to handle comment entities.
 */
@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    Collection<CommentEntity> findByEventId(long eventId);

    Collection<CommentEntity> findByPosterId(long userId);
}
