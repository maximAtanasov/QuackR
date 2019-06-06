package de.webtech.quackr.persistence.comment;

import de.webtech.quackr.persistence.CrudRepository;
import de.webtech.quackr.persistence.event.EventEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Simple CRUD repository to handle comment entities.
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommentRepository extends CrudRepository<CommentEntity, Long> {

    public CommentRepository() {
        super(CommentEntity.class);
    }

    /**
     * Finds all comments by their corresponding events.
     * @param eventId Id of the event.
     * @return All comments in the event.
     */
    public Collection<CommentEntity> findByEventId(long eventId){
        return entityManager.createQuery(
                "SELECT c FROM  de.webtech.quackr.persistence.comment.CommentEntity c WHERE c.eventId = :id", CommentEntity.class)
                .setParameter("id", eventId)
                .getResultList();
    }

    /**
     * Finds all comments by their corresponding poster (user).
     * @param userId Id of the user.
     * @return All comments of the user.
     */
    public Collection<CommentEntity> findByPosterId(long userId){
        return entityManager.createQuery(
                "SELECT c FROM  de.webtech.quackr.persistence.comment.CommentEntity c WHERE c.posterId = :id", CommentEntity.class)
                .setParameter("id", userId)
                .getResultList();
    }

    /**
     * Deletes a CommentEntity from the database.
     * The comment is first removed from the event it belongs to,
     * so as to not break integrity constrains.
     * @param obj The entity to delete.
     */
    @Override
    public void delete(CommentEntity obj) {
        EventEntity eventEntity = entityManager.find(EventEntity.class, obj.getEventId());
        eventEntity.getComments().remove(obj);
        entityManager.persist(eventEntity);
        entityManager.remove(obj);
    }
}
