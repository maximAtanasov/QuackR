package de.webtech.quackr.persistence.user;

import de.webtech.quackr.persistence.IRepository;
import de.webtech.quackr.persistence.event.EventEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

/**
 * Simple CRUD repository for handling users.
 */
@Repository
@Transactional
public class UserRepository implements IRepository<UserEntity, Long> {

    @PersistenceContext
    protected EntityManager entityManager;

    public boolean existsByUsername(String username){
        return findByUsername(username) != null;
    }

    public UserEntity findByUsername(String username){
        try {
            return entityManager.createQuery(
                    "SELECT u FROM  UserEntity u WHERE u.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public void save(UserEntity obj) {
        entityManager.persist(obj);
    }

    @Override
    public void delete(UserEntity obj) {
        Collection<EventEntity> eventEntities = entityManager.createQuery("SELECT e FROM  EventEntity e", EventEntity.class).getResultList();
        for(EventEntity e : eventEntities){
            if(e.getOrganizer().equals(obj)){
                entityManager.remove(e);
            } else {
                e.getComments().removeIf(c -> c.getPosterId().equals(obj.getId()));
                e.getAttendees().removeIf(attendee -> attendee.equals(obj));
                entityManager.persist(e);
            }
        }
        entityManager.persist(obj);
        entityManager.remove(obj);
    }

    @Override
    public Collection<UserEntity> findAll() {
        return entityManager.createQuery(
                "SELECT u FROM  UserEntity u", UserEntity.class)
                .getResultList();
    }
}
