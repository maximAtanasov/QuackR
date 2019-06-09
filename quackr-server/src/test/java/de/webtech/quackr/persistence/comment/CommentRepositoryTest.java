package de.webtech.quackr.persistence.comment;

import de.webtech.quackr.persistence.RepositoryTestTemplate;
import de.webtech.quackr.persistence.event.EventEntity;
import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRole;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CommentRepositoryTest extends RepositoryTestTemplate {

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Tests the findByUserId() method of the repository.
     */
    @Test
    public void testFindCommentsByUserId(){
        UserEntity userEntity1 = new UserEntity("testUser1", "testPassword", 50L, UserRole.USER);
        entityManager.persist(userEntity1);

        UserEntity userEntity2 = new UserEntity("testUser1", "testPassword", 50L, UserRole.USER);
        entityManager.persist(userEntity2);

        EventEntity eventEntity1 = new EventEntity();
        eventEntity1.setAttendeeLimit(10);
        eventEntity1.setDate(new Date());
        eventEntity1.setDescription("TEST");
        eventEntity1.setTitle("TEST_EVENT");
        eventEntity1.setLocation("TEST");
        eventEntity1.setOrganizer(userEntity1);

        entityManager.persist(eventEntity1);

        CommentEntity commentEntity1 = new CommentEntity();
        commentEntity1.setPosterId(userEntity1.getId());
        commentEntity1.setEventId(eventEntity1.getId());
        commentEntity1.setDatePosted(new Date());
        commentEntity1.setText("TEST_COMMNET");

        CommentEntity commentEntity2 = new CommentEntity();
        commentEntity2.setPosterId(userEntity1.getId());
        commentEntity2.setEventId(eventEntity1.getId());
        commentEntity2.setDatePosted(new Date());
        commentEntity2.setText("TEST_COMMNET_2");

        entityManager.persist(commentEntity1);
        entityManager.persist(commentEntity2);

        Assert.assertEquals(2, commentRepository.findByPosterId(userEntity1.getId()).size());

    }

    /**
     * Tests the findByEventId() method of the repository.
     */
    @Test
    public void testFindCommentsByEventId(){
        UserEntity userEntity1 = new UserEntity("testUser1", "testPassword", 50L, UserRole.USER);
        entityManager.persist(userEntity1);

        UserEntity userEntity2 = new UserEntity("testUser1", "testPassword", 50L, UserRole.USER);
        entityManager.persist(userEntity2);

        EventEntity eventEntity1 = new EventEntity();
        eventEntity1.setAttendeeLimit(10);
        eventEntity1.setDate(new Date());
        eventEntity1.setDescription("TEST");
        eventEntity1.setTitle("TEST_EVENT");
        eventEntity1.setLocation("TEST");
        eventEntity1.setOrganizer(userEntity1);

        entityManager.persist(eventEntity1);

        CommentEntity commentEntity1 = new CommentEntity();
        commentEntity1.setPosterId(userEntity1.getId());
        commentEntity1.setEventId(eventEntity1.getId());
        commentEntity1.setDatePosted(new Date());
        commentEntity1.setText("TEST_COMMNET");

        CommentEntity commentEntity2 = new CommentEntity();
        commentEntity2.setPosterId(userEntity1.getId());
        commentEntity2.setEventId(eventEntity1.getId());
        commentEntity2.setDatePosted(new Date());
        commentEntity2.setText("TEST_COMMNET_2");

        entityManager.persist(commentEntity1);
        entityManager.persist(commentEntity2);

        Assert.assertEquals(2, commentRepository.findByEventId(eventEntity1.getId()).size());
    }

    /**
     * Tests the delete method of the repository.
     */
    @Test
    public void testDeleteComment(){
        UserEntity userEntity1 = new UserEntity("testUser1", "testPassword", 50L, UserRole.USER);
        entityManager.persist(userEntity1);

        EventEntity eventEntity1 = new EventEntity();
        eventEntity1.setAttendeeLimit(10);
        eventEntity1.setDate(new Date());
        eventEntity1.setDescription("TEST");
        eventEntity1.setTitle("TEST_EVENT");
        eventEntity1.setLocation("TEST");
        eventEntity1.setOrganizer(userEntity1);

        entityManager.persist(eventEntity1);

        CommentEntity commentEntity1 = new CommentEntity();
        commentEntity1.setPosterId(userEntity1.getId());
        commentEntity1.setEventId(eventEntity1.getId());
        commentEntity1.setDatePosted(new Date());
        commentEntity1.setText("TEST_COMMNET");

        entityManager.persist(commentEntity1);

        commentRepository.delete(commentEntity1);

        Assert.assertFalse(commentRepository.findById(commentEntity1.getId()).isPresent());
        Assert.assertNotNull(entityManager.find(EventEntity.class, eventEntity1.getId()));
    }
}
