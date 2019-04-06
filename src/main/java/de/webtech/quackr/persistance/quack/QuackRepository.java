package de.webtech.quackr.persistance.quack;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuackRepository extends CrudRepository<QuackEntity, Long> {
}
