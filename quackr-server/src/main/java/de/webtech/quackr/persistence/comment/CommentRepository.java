package de.webtech.quackr.persistence.comment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Simple CRUD repository to handle comment entities.
 */
@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

    /**
     * Finds all comments by their corresponding events.
     * @param eventId Id of the event.
     * @return All comments in the event.
     */
    Collection<CommentEntity> findByEventId(long eventId);

    /**
     * Finds all comments by their corresponding poster (user).
     * @param userId Id of the user.
     * @return All comments of the user.
     */
    Collection<CommentEntity> findByPosterId(long userId);
}
