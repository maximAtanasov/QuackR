package de.webtech.quackr.persistence.event;

import de.webtech.quackr.persistence.CrudRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Simple CRUD repository to handle event entities.
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EventRepository extends CrudRepository<EventEntity, Long> {

    public EventRepository() {
        super(EventEntity.class);
    }

    /**
     * Finds all events organized by the user with the given id.
     * @param userId The id of the user.
     * @return All events organized by the user.
     */
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

    /**
     * Deletes an EventEntity while keeping integrity constrains.
     * @param obj The entity to delete.
     */
    @Override
    public void delete(EventEntity obj) {
        obj.getAttendees().clear();
        entityManager.persist(obj);
        entityManager.remove(obj);
    }
}
