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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
     * @throws UserNotFoundException Not thrown in this test
     */
    @Test
    public void testCreateComment() throws EventNotFoundException, UserNotFoundException {
        when(commentService.createComment(any(), anyLong()))
                .thenReturn(testGetResource);

        // Test JSON
        HttpEntity<CreateCommentResource> entity1 = new HttpEntity<>(testCreateResource, headersJSON);

        ResponseEntity<GetCommentResource> result1 = this.restTemplate.exchange("/api/comments/event/1", HttpMethod.POST, entity1, GetCommentResource.class);
        assertEquals(HttpStatus.CREATED, result1.getStatusCode());
        assertEquals(testGetResource, result1.getBody());

        // Test XML
        HttpEntity<CreateCommentResource> entity2 = new HttpEntity<>(testCreateResource, headersXML);

        ResponseEntity<GetCommentResource> result2 = this.restTemplate.exchange("/api/comments/event/1", HttpMethod.POST, entity2, GetCommentResource.class);
        assertEquals(HttpStatus.CREATED, result2.getStatusCode());
        assertEquals(testGetResource, result2.getBody());
    }

    /**
     * Tests that a GET request to the /comments/event/{eventId} endpoint return proper JSON.
     * @throws EventNotFoundException Not thrown in this test.
     */
    @Test
    public void testGetCommentsForEvent() throws EventNotFoundException {
        when(commentService.getCommentsForEvent(anyLong()))
                .thenReturn(Collections.singletonList(testGetResource));

        GetCommentResource[] expected = {testGetResource};

        // Test JSON
        HttpEntity<String> entity1 = new HttpEntity<>(headersJSON);

        ResponseEntity<GetCommentResource[]> result1 = this.restTemplate.exchange("/api/comments/event/1", HttpMethod.GET, entity1, GetCommentResource[].class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertArrayEquals(expected, result1.getBody());

        // Test XML
        HttpEntity<CreateCommentResource> entity2 = new HttpEntity<>(testCreateResource, headersXML);

        ResponseEntity<GetCommentResource[]> result2 = this.restTemplate.exchange("/api/comments/event/1", HttpMethod.GET, entity2, GetCommentResource[].class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertArrayEquals(expected, result2.getBody());
    }

    /**
     * Tests that a GET request to the /comments/user/{userId} endpoint return proper JSON.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testGetCommentsForUser() throws UserNotFoundException {
        when(commentService.getCommentsForUser(anyLong()))
                .thenReturn(Collections.singletonList(testGetResource));

        GetCommentResource[] expected = {testGetResource};

        // Test JSON
        HttpEntity<String> entity1 = new HttpEntity<>(headersJSON);

        ResponseEntity<GetCommentResource[]> result1 = this.restTemplate.exchange("/api/comments/user/1", HttpMethod.GET, entity1, GetCommentResource[].class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertArrayEquals(expected, result1.getBody());

        // Test XML
        HttpEntity<CreateCommentResource> entity2 = new HttpEntity<>(testCreateResource, headersXML);

        ResponseEntity<GetCommentResource[]> result2 = this.restTemplate.exchange("/api/comments/user/1", HttpMethod.GET, entity2, GetCommentResource[].class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertArrayEquals(expected, result2.getBody());
    }

    /**
     * Tests that a GET request to the /comments/{commentId} endpoint returns the proper JSON.
     * @throws CommentNotFoundException Not thrown in this test.
     */
    @Test
    public void testGetCommentById() throws CommentNotFoundException {
        when(commentService.getComment(anyLong()))
                .thenReturn(testGetResource);

        // Test JSON
        HttpEntity<CreateCommentResource> entity1 = new HttpEntity<>(headersJSON);

        ResponseEntity<GetCommentResource> result1 = this.restTemplate.exchange("/api/comments/1", HttpMethod.GET, entity1, GetCommentResource.class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(testGetResource, result1.getBody());

        // Test XML
        HttpEntity<CreateCommentResource> entity2 = new HttpEntity<>(headersXML);

        ResponseEntity<GetCommentResource> result2 = this.restTemplate.exchange("/api/comments/1", HttpMethod.GET, entity2, GetCommentResource.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(testGetResource, result2.getBody());
    }

    /**
     * Tests that a POST request to the /comments/{commentId} endpoint returns the proper JSON.
     * @throws CommentNotFoundException Not thrown in this test.
     * @throws CannotChangePosterIdException Not thrown in this test.
     */
    @Test
    public void testEditComment() throws CommentNotFoundException, CannotChangePosterIdException {

        when(commentService.editComment(any(), anyLong()))
                .thenReturn(testGetResource);

        // Test JSON
        HttpEntity<CreateCommentResource> entity1 = new HttpEntity<>(testCreateResource, headersJSON);

        ResponseEntity<GetCommentResource> result1 = this.restTemplate.exchange("/api/comments/1", HttpMethod.POST, entity1, GetCommentResource.class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(testGetResource, result1.getBody());

        // Test XML
        HttpEntity<CreateCommentResource> entity2 = new HttpEntity<>(testCreateResource, headersXML);

        ResponseEntity<GetCommentResource> result2 = this.restTemplate.exchange("/api/comments/1", HttpMethod.POST, entity2, GetCommentResource.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(testGetResource, result2.getBody());
    }

    /**
     * Tests that a DELETE request to the comments/{commentId} endpoint calls the correct service method.
     * @throws CommentNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteComment() throws CommentNotFoundException {
        HttpEntity entity = new HttpEntity<>(headersXML);
        ResponseEntity result2 = this.restTemplate.exchange("/api/comments/3", HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        Mockito.verify(commentService, Mockito.times(1)).deleteComment(3L);
    }
}
