package de.webtech.quackr.service.comment;

import de.webtech.quackr.persistence.comment.CommentEntity;
import de.webtech.quackr.persistence.comment.CommentRepository;
import de.webtech.quackr.persistence.event.EventEntity;
import de.webtech.quackr.persistence.event.EventRepository;
import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRepository;
import de.webtech.quackr.service.ServiceTestTemplate;
import de.webtech.quackr.service.comment.resources.CreateCommentResource;
import de.webtech.quackr.service.comment.resources.GetCommentResource;
import de.webtech.quackr.service.event.EventNotFoundException;
import de.webtech.quackr.service.user.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class CommentServiceTest extends ServiceTestTemplate {

    @MockBean
    UserRepository userRepository;

    @MockBean
    EventRepository eventRepository;

    @MockBean
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    /**
     * Mocks all methods from the userRepository and eventRepository that need mocking.
     * Returns example/dummy data where required.
     */
    @Before
    public void setUp() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(new UserEntity("testUser", "testPassword", 0L)));

        Mockito.when(userRepository.findById(7L))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(new UserEntity("testUser", "testPassword", 0L),
                        new UserEntity("testUser2", "testPassword2", 50L)));

        Mockito.when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito.when(userRepository.existsById(7L))
                .thenReturn(false);

        Mockito.when(userRepository.findByUsername(any()))
                .thenReturn(new UserEntity("testUser", "testPassword3", 10L));

        Mockito.when(userRepository.save(any())).thenReturn(new UserEntity("testUser", "testPassword", 10L));

        EventEntity entity = new EventEntity();
        entity.setTitle("BBQ");
        entity.setDescription("BBQ at Stan");
        entity.setAttendeeLimit(20L);
        entity.setOrganizer(new UserEntity());
        entity.setLocation("Stan's place");
        entity.setDate(new Date());
        entity.setPublic(true);
        entity.setAttendees(new ArrayList<>());
        entity.setId(1L);

        Mockito.when(eventRepository.findByOrganizerId(anyLong())).thenReturn(Collections.singletonList(entity));
        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(eventRepository.findById(7L)).thenReturn(Optional.empty());
        Mockito.when(eventRepository.findAll()).thenReturn(Collections.singletonList(entity));

        Mockito.when(eventRepository.existsById(1L))
                .thenReturn(true);

        Mockito.when(eventRepository.existsById(7L))
                .thenReturn(false);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText("text");
        commentEntity.setId(1L);
        commentEntity.setPosterId(1L);
        commentEntity.setDatePosted(new Date());
        commentEntity.setEventId(1L);
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));
        Mockito.when(commentRepository.findByPosterId(1L)).thenReturn(Collections.singletonList(commentEntity));
        Mockito.when(commentRepository.findByEventId(1L)).thenReturn(Collections.singletonList(commentEntity));
        Mockito.when(commentRepository.findByPosterId(7L)).thenReturn(new ArrayList<>());
        Mockito.when(commentRepository.findByEventId(7L)).thenReturn(new ArrayList<>());
        Mockito.when(commentRepository.findById(7L)).thenReturn(Optional.empty());
        Mockito.when(commentRepository.findAll()).thenReturn(Collections.singletonList(commentEntity));

        Mockito.when(commentRepository.existsById(1L))
                .thenReturn(true);

        Mockito.when(commentRepository.existsById(7L))
                .thenReturn(false);
    }

    /**
     * Tests the getCommentById() method of the service.
     * @throws CommentNotFoundException Doesn't throw in this test.
     */
    @Test
    public void testGetEventById() throws CommentNotFoundException {
        GetCommentResource result = commentService.getComment(1L);
        Assert.assertEquals("text", result.getText());
    }

    /**
     * Tests that the getCommentById() method of the service throws an exception if the comment is not found.
     * @throws CommentNotFoundException Checked in this test.
     */
    @Test(expected = CommentNotFoundException.class)
    public void testGetEventByIdThrowsExceptionIfEventNotFound() throws CommentNotFoundException {
        commentService.getComment(7L);
    }

    /**
     * Tests the getCommentsForEvent() method of the service.
     * @throws EventNotFoundException Not thrown in this test
     */
    @Test
    public void testGetCommentsForEvent() throws EventNotFoundException {
        Collection<GetCommentResource> result = commentService.getCommentsForEvent(1L);

        Assert.assertEquals(1, result.size());
        Iterator<GetCommentResource> it = result.iterator();
        Assert.assertEquals("text", it.next().getText());
    }

    /**
     * Tests the getCommentsForUser() method of the service.
     * @throws UserNotFoundException Not thrown in this test
     */
    @Test
    public void testGetCommentsForUser() throws UserNotFoundException {
        Collection<GetCommentResource> result = commentService.getCommentsForUser(1L);

        Assert.assertEquals(1, result.size());
        Iterator<GetCommentResource> it = result.iterator();
        Assert.assertEquals("text", it.next().getText());
    }

    /**
     * Tests that the getCommentsForUser() method of the service throws an Exception
     * if the user is not found.
     * @throws UserNotFoundException Checked in this test.
     */
    @Test(expected = UserNotFoundException.class)
    public void testGetCommentsForUserThrowsExceptionIfUserNotFound() throws UserNotFoundException {
        commentService.getCommentsForUser(7L);
    }

    /**
     * Tests that the getCommentsForEvent() method of the service throws an Exception
     * if the event is not found.
     * @throws EventNotFoundException Checked in this test.
     */
    @Test(expected = EventNotFoundException.class)
    public void testGetCommentsForEventThrowsExceptionIfEventNotFound() throws EventNotFoundException {
        commentService.getCommentsForEvent(7L);
    }

    /**
     * Tests the createComment() method of the service.
     * @throws EventNotFoundException Doesn't throw in this test.
     */
    @Test
    public void testCreateComment() throws EventNotFoundException {
        CreateCommentResource resource = new CreateCommentResource();
        resource.setText("BBQ1");
        resource.setPosterId(1L);

        GetCommentResource result = commentService.createComment(resource, 1L);
        Mockito.verify(commentRepository, Mockito.times(1)).save(any());
        Assert.assertEquals(resource.getText(), result.getText());
        Assert.assertEquals(resource.getPosterId(), result.getPosterId());
        Assert.assertEquals(1L, result.getEventId().longValue());
        Assert.assertNotNull(result.getDatePosted());
    }

    /**
     * Tests that the createComment() method of the service
     * throws an exception if the event is not found.
     * @throws EventNotFoundException Checked in this test.
     */
    @Test(expected = EventNotFoundException.class)
    public void testCreateEventThrowsExceptionIfUserNotFound() throws EventNotFoundException {
        CreateCommentResource resource = new CreateCommentResource();

        commentService.createComment(resource, 7L);
        Mockito.verify(commentRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests the editComment() method of the service.
     * @throws CommentNotFoundException Not thrown in this test.
     */
    @Test
    public void testEditComment() throws CommentNotFoundException {
        CreateCommentResource resource = new CreateCommentResource();
        resource.setText("BBQ1");
        resource.setPosterId(1L);

        GetCommentResource result = commentService.editComment(resource, 1L);
        Mockito.verify(commentRepository, Mockito.times(1)).save(any());
        Assert.assertEquals(resource.getText(), result.getText());
        Assert.assertEquals(resource.getPosterId(), result.getPosterId());
        Assert.assertEquals(1L, result.getEventId().longValue());
        Assert.assertNotNull(result.getDatePosted());
    }

    /**
     * Tests that the editComment() method of the service throws
     * an exception in the comment is not found.
     * @throws CommentNotFoundException Checked in this test.
     */
    @Test(expected = CommentNotFoundException.class)
    public void testEditEventThrowsExceptionIfEventNotFound() throws CommentNotFoundException {
        CreateCommentResource resource = new CreateCommentResource();
        commentService.editComment(resource, 7L);
        Mockito.verify(commentRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests the deleteComment() method of the service.
     * @throws CommentNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteComment() throws CommentNotFoundException {
        commentService.deleteComment(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(1L);
    }

    /**
     * Tests that the deleteComment() method of the service throws an
     * exception if the comment is not found.
     * @throws CommentNotFoundException Checked in this test.
     */
    @Test(expected = CommentNotFoundException.class)
    public void testDeleteCommentThrowsExceptionIfCommentNotFound() throws CommentNotFoundException {
        commentService.deleteComment(7L);
        Mockito.verify(commentRepository, Mockito.times(0)).deleteById(7L);
    }
}
