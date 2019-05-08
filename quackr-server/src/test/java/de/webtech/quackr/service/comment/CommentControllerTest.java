package de.webtech.quackr.service.comment;

import de.webtech.quackr.service.ControllerTestTemplate;
import de.webtech.quackr.service.comment.resources.CreateCommentResource;
import de.webtech.quackr.service.comment.resources.GetCommentResource;
import de.webtech.quackr.service.event.EventNotFoundException;
import de.webtech.quackr.service.user.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


public class CommentControllerTest extends ControllerTestTemplate {

    @MockBean
    private CommentService commentService;

    private CreateCommentResource testCreateResource;
    private GetCommentResource testGetResource;

    /**
     * Tests set up
     */
    @Before
    public void setUp() {
        testGetResource = new GetCommentResource(1L, "text", new Date(), 1L, 2L);
        testCreateResource = new CreateCommentResource("text", 1L);
    }

    /**
     * Tests that a POST request to the /comments/event/{eventId} endpoint returns proper JSON.
     * @throws EventNotFoundException Not thrown in this test
     */
    @Test
    public void testCreateEvent() throws EventNotFoundException {
        Mockito.when(commentService.createComment(any(), anyLong()))
                .thenReturn(testGetResource);

        ResponseEntity<GetCommentResource> entity =
                this.restTemplate.postForEntity("/comments/event/1", testCreateResource, GetCommentResource.class);
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(testGetResource , entity.getBody());
    }

    /**
     * Tests that a GET request to the /comments/event/{eventId} endpoint return proper JSON.
     * @throws EventNotFoundException Not thrown in this test.
     */
    @Test
    public void testGetCommentsForEvent() throws EventNotFoundException {
        Mockito.when(commentService.getCommentsForEvent(anyLong()))
                .thenReturn(Collections.singletonList(testGetResource));

        ResponseEntity<GetCommentResource[]> entity = this.restTemplate.getForEntity("/comments/event/1", GetCommentResource[].class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());

        GetCommentResource[] expected = {testGetResource};
        assertArrayEquals(expected, entity.getBody());
    }

    /**
     * Tests that a GET request to the /comments/user/{userId} endpoint return proper JSON.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testGetCommentsForUser() throws UserNotFoundException {
        Mockito.when(commentService.getCommentsForUser(anyLong()))
                .thenReturn(Collections.singletonList(testGetResource));

        ResponseEntity<GetCommentResource[]> entity = this.restTemplate.getForEntity("/comments/user/1", GetCommentResource[].class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());

        GetCommentResource[] expected = {testGetResource};
        assertArrayEquals(expected, entity.getBody());
    }

    /**
     * Tests that a GET request to the /comments/{commentId} endpoint returns the proper JSON.
     * @throws CommentNotFoundException Not thrown in this test.
     */
    @Test
    public void testGetCommentById() throws CommentNotFoundException {
        Mockito.when(commentService.getComment(anyLong()))
                .thenReturn(testGetResource);

        ResponseEntity<GetCommentResource> entity = this.restTemplate.getForEntity("/comments/1", GetCommentResource.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(testGetResource, entity.getBody());
    }

    /**
     * Tests that a POST request to the /comments/{commentId} endpoint returns the proper JSON.
     * @throws CommentNotFoundException Not thrown in this test.
     */
    @Test
    public void testEditComment() throws CommentNotFoundException {

        Mockito.when(commentService.editComment(any(), anyLong()))
                .thenReturn(testGetResource);

        ResponseEntity<GetCommentResource> entity = this.restTemplate.postForEntity("/comments/1",
                testCreateResource, GetCommentResource.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(testGetResource, entity.getBody());
    }

    /**
     * Tests that a DELETE request to the comments/{commentId} endpoint calls the correct service method.
     * @throws CommentNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteComment() throws CommentNotFoundException {
        this.restTemplate.delete("/comments/3");
        Mockito.verify(commentService, Mockito.times(1)).deleteComment(3L);
    }
}
