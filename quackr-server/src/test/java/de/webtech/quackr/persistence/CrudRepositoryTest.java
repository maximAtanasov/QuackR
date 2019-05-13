package de.webtech.quackr.persistence;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Tests all methods of the CrudRepository (abstract) class.
 * The class is instantiated in TestRepository.java
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CrudRepositoryTest extends RepositoryTestTemplate {

    @Entity
    @Data
    class TestEntity{

        @Id
        @GeneratedValue
        private Long id;
    }

    @Autowired
    private TestRepository testRepository;

    /**
     * Tests the save method of the crud repository.
     */
    @Test
    public void testSaveEntity(){
        TestEntity testEntity = new TestEntity();
        testRepository.save(testEntity);
        Assert.assertNotNull(entityManager.find(TestEntity.class, testEntity.getId()));
    }

    /**
     * Tests the findById() method of the crud repository.
     */
    @Test
    public void testFindEntityById(){
        TestEntity testEntity = new TestEntity();
        entityManager.persist(testEntity);
        Assert.assertEquals(testEntity, testRepository.findById(testEntity.getId()).get());
    }

    /**
     * Tests the findByAll() method of the crud repository.
     */
    @Test
    public void testFindAllEntities(){
        entityManager.persist(new TestEntity());
        entityManager.persist(new TestEntity());
        entityManager.persist(new TestEntity());

        Assert.assertEquals(testRepository.findAll().size(), 3);
    }


    /**
     * Tests the delete() method of the crud repository.
     */
    @Test
    public void testDeleteEntity(){
        TestEntity testEntity = new TestEntity();
        entityManager.persist(testEntity);
        testRepository.delete(testEntity);
        Assert.assertNull(entityManager.find(TestEntity.class, testEntity.getId()));
    }
}
