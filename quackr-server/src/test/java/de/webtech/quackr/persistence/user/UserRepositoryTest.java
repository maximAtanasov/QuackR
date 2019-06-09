package de.webtech.quackr.persistence.user;


import de.webtech.quackr.persistence.RepositoryTestTemplate;
import de.webtech.quackr.persistence.comment.CommentEntity;
import de.webtech.quackr.persistence.event.EventEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;

public class UserRepositoryTest extends RepositoryTestTemplate {

    @Autowired
    private UserRepository userRepository;

    /**
     * Tests the findByUsername() method of the repository
     */
    @Test
    public void testFindUserByUsername(){
        entityManager.persist(new UserEntity("testUser", "testPassword", 50L, UserRole.USER));
        Assert.assertEquals(userRepository.findByUsername("testUser").getUsername(), "testUser");
    }

    /**
     * Tests the existsByUsername() method of the repository
     */
    @Test
    public void testExistsByUsername(){
        entityManager.persist(new UserEntity("testUser4", "testPassword", 50L, UserRole.USER));
        Assert.assertTrue(userRepository.existsByUsername("testUser4"));
    }

    /**
     * Tests the findByRole() method of the repository
     */
    @Test
    public void testFindByRole(){
        entityManager.persist(new UserEntity("testUser4", "testPassword", 50L, UserRole.USER));
        entityManager.persist(new UserEntity("testUser5", "testPassword", 50L, UserRole.ADMIN));

        Collection<UserEntity> userEntities = userRepository.findByRole(UserRole.ADMIN);
        Assert.assertEquals(1, userEntities.size());
        Assert.assertEquals("testUser5", userEntities.iterator().next().getUsername());
    }

    /**
     * Tests the delete() method of the repository
     */
    @Test
    public void testDeleteUser(){
        UserEntity userEntity = new UserEntity("testUser1", "testPassword", 50L, UserRole.USER);
        entityManager.persist(userEntity);

        UserEntity userEntity2 = new UserEntity("testUser1", "testPassword", 50L, UserRole.USER);
        entityManager.persist(userEntity2);

        EventEntity eventEntity = new EventEntity();
        eventEntity.setAttendeeLimit(10);
        eventEntity.setDate(new Date());
        eventEntity.setDescription("TEST");
        eventEntity.setTitle("TEST");
        eventEntity.setLocation("TEST");
        eventEntity.setOrganizer(userEntity);

        EventEntity eventEntity1 = new EventEntity();
        eventEntity1.setAttendeeLimit(10);
        eventEntity1.setDate(new Date());
        eventEntity1.setDescription("TEST");
        eventEntity1.setTitle("TEST");
        eventEntity1.setLocation("TEST");
        eventEntity1.setOrganizer(userEntity2);
        eventEntity.getAttendees().add(userEntity);

        entityManager.persist(eventEntity);
        entityManager.persist(eventEntity1);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setDatePosted(new Date());
        commentEntity.setEventId(eventEntity1.getId());
        commentEntity.setPosterId(userEntity.getId());
        commentEntity.setText("TEST");

        entityManager.persist(commentEntity);

        eventEntity1.getComments().add(commentEntity);
        entityManager.persist(eventEntity1);

        userRepository.delete(userEntity);

        Assert.assertNull(entityManager.find(UserEntity.class, userEntity.getId()));
        Assert.assertNull(entityManager.find(CommentEntity.class, commentEntity.getId()));
        Assert.assertNull(entityManager.find(EventEntity.class, eventEntity.getId()));
        Assert.assertNotNull(entityManager.find(EventEntity.class, eventEntity1.getId()));
        Assert.assertEquals(0, entityManager.find(EventEntity.class, eventEntity1.getId()).getAttendees().size());
    }
}
