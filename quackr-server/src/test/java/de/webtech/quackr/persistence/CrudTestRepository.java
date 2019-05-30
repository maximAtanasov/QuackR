package de.webtech.quackr.persistence;

import org.springframework.stereotype.Repository;

/**
 * A simple test instance of CrudRepository
 */
@Repository
public class CrudTestRepository extends CrudRepository<CrudRepositoryTest.TestEntity, Long>{

    public CrudTestRepository() {
        super(CrudRepositoryTest.TestEntity.class);
    }
}
