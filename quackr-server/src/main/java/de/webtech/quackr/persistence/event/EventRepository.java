package de.webtech.quackr.persistence.event;

import de.webtech.quackr.persistence.IRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Simple CRUD repository to handle event entities.
 */
@Repository
@Transactional
public class EventRepository implements IRepository<EventEntity, Long> {

    @PersistenceContext
    protected EntityManager entityManager;

    public Collection<EventEntity> findByOrganizerId(long userId){
        try {
            return entityManager.createQuery(
                    "SELECT e FROM  EventEntity e WHERE e.organizer.id = :id", EventEntity.class)
                    .setParameter("id", userId)
                    .getResultList();
        }catch (NoResultException e){
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<EventEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(EventEntity.class, id));
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public void save(EventEntity obj) {
        entityManager.persist(obj);
    }

    @Override
    public void delete(EventEntity obj) {
        obj.getAttendees().clear();
        entityManager.persist(obj);
        entityManager.remove(obj);
    }

    @Override
    public Collection<EventEntity> findAll() {
        try {
            return entityManager.createQuery(
                    "SELECT e FROM  EventEntity e", EventEntity.class)
                    .getResultList();
        }catch (NoResultException e){
            return new ArrayList<>();
        }
    }
}
