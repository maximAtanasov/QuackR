package de.webtech.quackr.persistence.comment;

import de.webtech.quackr.persistence.IRepository;
import de.webtech.quackr.persistence.event.EventEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

/**
 * Simple CRUD repository to handle comment entities.
 */
@Repository
@Transactional
public class CommentRepository implements IRepository<CommentEntity, Long> {

    @PersistenceContext
    protected EntityManager entityManager;


    public Collection<CommentEntity> findByEventId(long eventId){
        return entityManager.createQuery(
                "SELECT c FROM  de.webtech.quackr.persistence.comment.CommentEntity c WHERE c.eventId = :id", CommentEntity.class)
                .setParameter("id", eventId)
                .getResultList();
    }

    public Collection<CommentEntity> findByPosterId(long userId){
        return entityManager.createQuery(
                "SELECT c FROM  de.webtech.quackr.persistence.comment.CommentEntity c WHERE c.posterId = :id", CommentEntity.class)
                .setParameter("id", userId)
                .getResultList();
    }

    public Optional<CommentEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(CommentEntity.class, id));
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public void save(CommentEntity obj) {
        entityManager.persist(obj);
    }

    public void delete(CommentEntity obj) {
        EventEntity eventEntity = entityManager.find(EventEntity.class, obj.getEventId());
        eventEntity.getComments().remove(obj);
        entityManager.persist(eventEntity);
        entityManager.remove(obj);
    }

    public Collection<CommentEntity> findAll() {
        return entityManager.createQuery(
                "SELECT c FROM  CommentEntity c", CommentEntity.class)
                .getResultList();
    }
}
