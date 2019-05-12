package de.webtech.quackr.persistence;

import java.util.Collection;
import java.util.Optional;

/**
 * An interface representing a simple CRUD repository
 * @param <T> Entity type
 * @param <E> Primary key
 */
public interface IRepository<T, E> {
    Optional<T> findById(E id);
    boolean existsById(E id);
    void save(T obj);
    void delete(T obj);
    Collection<T> findAll();
}
