package de.webtech.quackr.persistence;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Optional;

/**
 * A simple CRUD repository
 * implementation.
 * @param <T> Entity type
 * @param <E> Primary key
 */
@Transactional
public abstract class CrudRepository<T, E> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> clazz;

    /**
     *
     * @param clazz The class of the Entity. Required to perform queries.
     */
    public CrudRepository(Class<T> clazz){
        this.clazz = clazz;
    }

    /**
     * Find an entity in the DB given it's primary key.
     * @param id The PK of the entity.
     * @return  An Optional that either contains the entity or nothing if it's not found.
     */
    public Optional<T> findById(E id){
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    /**
     * Tells whether the entity exists or not given it's PK.
     * @param id The PK of the entity.
     * @return True if it exists and false otherwise.
     */
    public boolean existsById(E id){
        return findById(id).isPresent();
    }

    /**
     * Saves an entity in the database.
     * @param obj The entity to save.
     */
    public void save(T obj){
        entityManager.persist(obj);
    }

    /**
     * Deletes an entity from the database.
     * @param obj The entity to delete.
     */
    public void delete(T obj){
        entityManager.remove(obj);
    }

    /**
     * Deletes all entities from the database.
     */
    public void deleteAll(){
        entityManager.createQuery(
                "DELETE FROM " + clazz.getName()).executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * Returns all entities of the given type.
     * @return All entities in the DB of type T.
     */
    public Collection<T> findAll(){
        return entityManager.createQuery(
                "SELECT c FROM " + clazz.getName() + " c", clazz)
                .getResultList();
    }
}
