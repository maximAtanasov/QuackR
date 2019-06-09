package de.webtech.quackr.persistence.event;

import de.webtech.quackr.persistence.RepositoryTestTemplate;
import de.webtech.quackr.persistence.comment.CommentEntity;
import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRole;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class EventRepositoryTest extends RepositoryTestTemplate {

    @Autowired
    private EventRepository eventRepository;

    /**
     * Tests the findByOrganizerId() method of the repository.
     */
    @Test
    public void testFindEventsByOrganizerId(){
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

        EventEntity eventEntity2 = new EventEntity();
        eventEntity2.setAttendeeLimit(10);
        eventEntity2.setDate(new Date());
        eventEntity2.setDescription("TEST");
        eventEntity2.setTitle("TEST_EVENT_2");
        eventEntity2.setLocation("TEST");
        eventEntity2.setOrganizer(userEntity2);

        entityManager.persist(eventEntity1);
        entityManager.persist(eventEntity2);

        Assert.assertEquals(1, eventRepository.findByOrganizerId(userEntity1.getId()).size());
        Assert.assertEquals("TEST_EVENT", eventRepository.findByOrganizerId(userEntity1.getId()).iterator().next().getTitle());
    }

    /**
     * Tests the delete method of the repository.
     */
    @Test
    public void testDeleteEvent(){
        UserEntity userEntity = new UserEntity("testUser1", "testPassword", 50L, UserRole.USER);
        entityManager.persist(userEntity);

        UserEntity userEntity2 = new UserEntity("testUser1", "testPassword", 50L, UserRole.USER);
        entityManager.persist(userEntity2);

        EventEntity eventEntity1 = new EventEntity();
        eventEntity1.setAttendeeLimit(10);
        eventEntity1.setDate(new Date());
        eventEntity1.setDescription("TEST");
        eventEntity1.setTitle("TEST");
        eventEntity1.setLocation("TEST");
        eventEntity1.setOrganizer(userEntity);
        eventEntity1.getAttendees().add(userEntity);

        EventEntity eventEntity2 = new EventEntity();
        eventEntity2.setAttendeeLimit(10);
        eventEntity2.setDate(new Date());
        eventEntity2.setDescription("TEST");
        eventEntity2.setTitle("TEST");
        eventEntity2.setLocation("TEST");
        eventEntity2.setOrganizer(userEntity2);

        entityManager.persist(eventEntity1);
        entityManager.persist(eventEntity2);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setDatePosted(new Date());
        commentEntity.setEventId(eventEntity2.getId());
        commentEntity.setPosterId(userEntity.getId());
        commentEntity.setText("TEST");

        entityManager.persist(commentEntity);

        eventEntity1.getComments().add(commentEntity);
        entityManager.persist(eventEntity1);

        eventRepository.delete(eventEntity1);

        Assert.assertNull(entityManager.find(EventEntity.class, eventEntity1.getId()));
        Assert.assertNull(entityManager.find(CommentEntity.class, commentEntity.getId()));
        Assert.assertNotNull(entityManager.find(UserEntity.class, userEntity.getId()));
        Assert.assertNotNull(entityManager.find(UserEntity.class, userEntity2.getId()));
    }
}
