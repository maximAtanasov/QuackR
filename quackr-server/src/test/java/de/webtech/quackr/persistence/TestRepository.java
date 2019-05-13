package de.webtech.quackr.persistence;

import org.springframework.stereotype.Repository;

/**
 * A simple test instance of CrudRepository
 */
@Repository
public class TestRepository extends CrudRepository<CrudRepositoryTest.TestEntity, Long>{

    public TestRepository() {
        super(CrudRepositoryTest.TestEntity.class);
    }
}
