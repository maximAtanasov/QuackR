package de.webtech.quackr.service.comment;

import de.webtech.quackr.persistence.comment.CommentEntity;
import de.webtech.quackr.persistence.comment.CommentRepository;
import de.webtech.quackr.persistence.event.EventEntity;
import de.webtech.quackr.persistence.event.EventRepository;
import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRepository;
import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.comment.resources.CreateCommentResource;
import de.webtech.quackr.service.comment.resources.GetCommentResource;
import de.webtech.quackr.service.event.EventNotFoundException;
import de.webtech.quackr.service.user.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

public class CommentServiceTest {

    private UserRepository userRepository = mock(UserRepository.class);

    private EventRepository eventRepository = mock(EventRepository.class);

    private CommentRepository commentRepository = mock(CommentRepository.class);

    private CommentService commentService;

    /**
     * Mocks all methods from the userRepository and eventRepository that need mocking.
     * Returns example/dummy data where required.
     */
    @Before
    public void setUp() {
        commentService = new CommentService(commentRepository, eventRepository, userRepository);
        Mockito.when(userRepository.findById(3L))
                .thenReturn(Optional.of(new UserEntity("testUser", "testPassword", 0L, UserRole.USER)));

        Mockito.when(userRepository.findById(7L))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(new UserEntity("testUser", "testPassword", 0L, UserRole.USER),
                        new UserEntity("testUser2", "testPassword2", 50L, UserRole.USER)));

        Mockito.when(userRepository.existsById(3L))
                .thenReturn(true);

        Mockito.when(userRepository.existsById(7L))
                .thenReturn(false);

        Mockito.when(userRepository.findByUsername(any()))
                .thenReturn(new UserEntity("testUser", "testPassword3", 10L, UserRole.USER));

        EventEntity entity = new EventEntity();
        entity.setTitle("BBQ");
        entity.setDescription("BBQ at Stan");
        entity.setAttendeeLimit(20);
        entity.setOrganizer(new UserEntity());
        entity.setLocation("Stan's place");
        entity.setDate(new Date());
        entity.setPublic(true);
        entity.setAttendees(new ArrayList<>());
        entity.setId(1L);

        Mockito.when(eventRepository.findByOrganizerId(anyLong())).thenReturn(Collections.singletonList(entity));
        Mockito.when(eventRepository.findById(2L)).thenReturn(Optional.of(entity));
        Mockito.when(eventRepository.findById(7L)).thenReturn(Optional.empty());
        Mockito.when(eventRepository.findAll()).thenReturn(Collections.singletonList(entity));

        Mockito.when(eventRepository.existsById(2L))
                .thenReturn(true);

        Mockito.when(eventRepository.existsById(7L))
                .thenReturn(false);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText("text");
        commentEntity.setId(1L);
        commentEntity.setPosterId(3L);
        commentEntity.setDatePosted(new Date());
        commentEntity.setEventId(2L);
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));
        Mockito.when(commentRepository.findByPosterId(3L)).thenReturn(Collections.singletonList(commentEntity));
        Mockito.when(commentRepository.findByEventId(2L)).thenReturn(Collections.singletonList(commentEntity));
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
    public void testGetCommentById() throws CommentNotFoundException {
        GetCommentResource result = commentService.getComment(1L);
        Assert.assertEquals("text", result.getText());
    }

    /**
     * Tests that the getCommentById() method of the service throws an exception if the comment is not found.
     * @throws CommentNotFoundException Checked in this test.
     */
    @Test(expected = CommentNotFoundException.class)
    public void testGetCommentByIdThrowsExceptionIfCommentNotFound() throws CommentNotFoundException {
        commentService.getComment(7L);
    }

    /**
     * Tests the getCommentsForEvent() method of the service.
     * @throws EventNotFoundException Not thrown in this test
     */
    @Test
    public void testGetCommentsForEvent() throws EventNotFoundException {
        Collection<GetCommentResource> result = commentService.getCommentsForEvent(2L);

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
        Collection<GetCommentResource> result = commentService.getCommentsForUser(3L);

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
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testCreateComment() throws EventNotFoundException, UserNotFoundException {
        CreateCommentResource resource = new CreateCommentResource();
        resource.setText("BBQ1");
        resource.setPosterId(3L);

        GetCommentResource result = commentService.createComment(resource, 2L);
        Mockito.verify(commentRepository, Mockito.times(1)).save(any());
        Assert.assertEquals(resource.getText(), result.getText());
        Assert.assertEquals(resource.getPosterId(), result.getPosterId());
        Assert.assertEquals(2L, result.getEventId().longValue());
        Assert.assertNotNull(result.getDatePosted());
    }

    /**
     * Tests that the createComment() method of the service
     * throws an exception if the event is not found.
     * @throws EventNotFoundException Checked in this test.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test(expected = EventNotFoundException.class)
    public void testCreateCommentThrowsExceptionIfEventNotFound() throws EventNotFoundException, UserNotFoundException {
        CreateCommentResource resource = new CreateCommentResource();

        commentService.createComment(resource, 7L);
        Mockito.verify(commentRepository, Mockito.times(0)).save(any());
    }



    /**
     * Tests that the createComment() method of the service
     * throws an exception if the event is not found.
     * @throws EventNotFoundException Not thrown in this test.
     * @throws UserNotFoundException Checked in this test.
     */
    @Test(expected = UserNotFoundException.class)
    public void testCreateCommentThrowsExceptionIfUserNotFound() throws EventNotFoundException, UserNotFoundException {
        CreateCommentResource resource = new CreateCommentResource();
        resource.setPosterId(7L);
        commentService.createComment(resource, 2L);
        Mockito.verify(commentRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests the editComment() method of the service.
     * @throws CommentNotFoundException Not thrown in this test.
     * @throws CannotChangePosterIdException Not thrown in this test.
     */
    @Test
    public void testEditComment() throws CommentNotFoundException, CannotChangePosterIdException {
        CreateCommentResource resource = new CreateCommentResource();
        resource.setText("BBQ1");
        resource.setPosterId(3L);

        GetCommentResource result = commentService.editComment(resource, 1L);
        Mockito.verify(commentRepository, Mockito.times(1)).save(any());
        Assert.assertEquals(resource.getText(), result.getText());
        Assert.assertEquals(resource.getPosterId(), result.getPosterId());
        Assert.assertEquals(2L, result.getEventId().longValue());
        Assert.assertNotNull(result.getDatePosted());
    }

    /**
     * Tests that the editComment() method of the service throws
     * an exception in the comment is not found.
     * @throws CommentNotFoundException Checked in this test.
     * @throws CannotChangePosterIdException Not thrown in this test.
     */
    @Test(expected = CommentNotFoundException.class)
    public void testEditEventThrowsExceptionIfEventNotFound() throws CommentNotFoundException, CannotChangePosterIdException {
        CreateCommentResource resource = new CreateCommentResource();
        commentService.editComment(resource, 7L);
        Mockito.verify(commentRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests that the editComment() method of the service throws
     * an exception in the posterId is changed.
     * @throws CommentNotFoundException Not thrown in this test.
     * @throws CannotChangePosterIdException Checked in this test.
     */
    @Test(expected = CannotChangePosterIdException.class)
    public void testEditEventThrowsExceptionIfPosterIdChanged() throws CommentNotFoundException, CannotChangePosterIdException {
        CreateCommentResource resource = new CreateCommentResource();
        resource.setText("BBQ1");
        resource.setPosterId(4L);

        GetCommentResource result = commentService.editComment(resource, 1L);
        Mockito.verify(commentRepository, Mockito.times(1)).save(any());
        Assert.assertEquals(resource.getText(), result.getText());
        Assert.assertEquals(resource.getPosterId(), result.getPosterId());
        Assert.assertEquals(2L, result.getEventId().longValue());
        Assert.assertNotNull(result.getDatePosted());
        Mockito.verify(commentRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests the deleteComment() method of the service.
     * @throws CommentNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteComment() throws CommentNotFoundException {
        commentService.deleteComment(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).delete(any());
    }

    /**
     * Tests that the deleteComment() method of the service throws an
     * exception if the comment is not found.
     * @throws CommentNotFoundException Checked in this test.
     */
    @Test(expected = CommentNotFoundException.class)
    public void testDeleteCommentThrowsExceptionIfCommentNotFound() throws CommentNotFoundException {
        commentService.deleteComment(7L);
        Mockito.verify(commentRepository, Mockito.times(0)).delete(any());
    }
}
