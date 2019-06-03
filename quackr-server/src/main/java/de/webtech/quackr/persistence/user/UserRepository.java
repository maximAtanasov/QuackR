package de.webtech.quackr.persistence.user;

import de.webtech.quackr.persistence.CrudRepository;
import de.webtech.quackr.persistence.event.EventEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Collection;

/**
 * Simple CRUD repository for handling users.
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserRepository extends CrudRepository<UserEntity, Long> {

    public UserRepository() {
        super(UserEntity.class);
    }

    /**
     * Tells whether a user exists given a username.
     * @param username The username to check for.
     * @return True if the user exists, false otherwise.
     */
    public boolean existsByUsername(String username){
        return findByUsername(username) != null;
    }

    /**
     * Finds a user given their username.
     * @param username The username to look for.
     * @return The user with the given username, or null.
     */
    public UserEntity findByUsername(String username){
        try {
            return entityManager.createQuery(
                    "SELECT u FROM  de.webtech.quackr.persistence.user.UserEntity u WHERE u.username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     * Finds all users with the given tole.
     * @param role The role to look for.
     * @return All users with the given role.
     */
    public Collection<UserEntity> findByRole(UserRole role){
        return entityManager.createQuery(
                "SELECT u FROM  de.webtech.quackr.persistence.user.UserEntity u WHERE u.role = :role", UserEntity.class)
                .setParameter("role", role)
                .getResultList();
    }

    /**
     * Deletes a UserEntity from the DB while keeping integrity constrains.
     * @param obj The entity to delete.
     */
    @Override
    public void delete(UserEntity obj) {
        Collection<EventEntity> eventEntities =
                entityManager.createQuery("SELECT e FROM  de.webtech.quackr.persistence.event.EventEntity e", EventEntity.class)
                .getResultList();
        for(EventEntity e : eventEntities){
            if(e.getOrganizer().equals(obj)){
                entityManager.remove(e);
            } else {
                e.getComments().forEach(c -> {
                    if(c.getPosterId().equals(obj.getId())){
                        entityManager.remove(c);
                    }
                });
                e.getComments().removeIf(c -> c.getPosterId().equals(obj.getId()));
                e.getAttendees().removeIf(attendee -> attendee.equals(obj));
                entityManager.persist(e);
            }
        }
        entityManager.persist(obj);
        entityManager.remove(obj);
    }
}
