package de.webtech.quackr.persistence;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

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
    private CrudTestRepository crudTestRepository;

    /**
     * Tests the save method of the crud repository.
     */
    @Test
    public void testSaveEntity(){
        TestEntity testEntity = new TestEntity();
        crudTestRepository.save(testEntity);
        Assert.assertNotNull(entityManager.find(TestEntity.class, testEntity.getId()));
    }

    /**
     * Tests the findById() method of the crud repository.
     */
    @Test
    public void testFindEntityById(){
        TestEntity testEntity = new TestEntity();
        entityManager.persist(testEntity);
        Assert.assertEquals(testEntity, crudTestRepository.findById(testEntity.getId()).get());
    }

    /**
     * Tests the findByAll() method of the crud repository.
     */
    @Test
    public void testFindAllEntities(){
        entityManager.persist(new TestEntity());
        entityManager.persist(new TestEntity());
        entityManager.persist(new TestEntity());

        Assert.assertEquals(crudTestRepository.findAll().size(), 3);
    }

    /**
     * Tests the deleteAll() method of the crud repository.
     */
    @Test
    public void testDeleteAll(){
        TestEntity testEntity1 = new TestEntity();
        TestEntity testEntity2 = new TestEntity();

        testEntity1 = entityManager.persist(testEntity1);
        testEntity2 = entityManager.persist(testEntity2);

        crudTestRepository.deleteAll();

        Assert.assertNull(entityManager.find(TestEntity.class, testEntity1.getId()));
        Assert.assertNull(entityManager.find(TestEntity.class, testEntity2.getId()));
    }


    /**
     * Tests the delete() method of the crud repository.
     */
    @Test
    public void testDeleteEntity(){
        TestEntity testEntity = new TestEntity();
        entityManager.persist(testEntity);
        crudTestRepository.delete(testEntity);
        Assert.assertNull(entityManager.find(TestEntity.class, testEntity.getId()));
    }
}
