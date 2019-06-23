package de.webtech.quackr.service.event;

import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.ControllerTestTemplate;
import de.webtech.quackr.service.event.resources.CreateEventResource;
import de.webtech.quackr.service.event.resources.GetEventResource;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


public class EventControllerTest extends ControllerTestTemplate {

    @MockBean
    private EventService eventService;

    private CreateEventResource testCreateResource;
    private GetEventResource testGetResource;

    /**
     * Tests set up
     */
    @Before
    public void setUp() {
        testGetResource = new GetEventResource();
        testGetResource.setAttendeeLimit(20);
        testGetResource.setDate(new Date(new Date().getTime()+1000));
        testGetResource.setDescription("something");
        testGetResource.setLocation("Somewhere");
        testGetResource.setPublic(true);
        testGetResource.setTitle("BBQ");
        testGetResource.setId(1L);

        testCreateResource = new CreateEventResource();
        testCreateResource.setAttendeeLimit(20);
        testCreateResource.setDate(new Date(new Date().getTime()+1000));
        testCreateResource.setDescription("something");
        testCreateResource.setLocation("Somewhere");
        testCreateResource.setPublic(true);
        testCreateResource.setTitle("BBQ");
        testGetResource.setAttendees(new ArrayList<>());
    }

    /**
     * Tests that a POST request to the /events/user/{userId} endpoint returns proper JSON.
     * @throws UserNotFoundException Not thrown in this test
     */
    @Test
    public void testCreateEvent() throws UserNotFoundException {
        Mockito.when(eventService.createEvent(any(), anyLong()))
                .thenReturn(testGetResource);

        // Test JSON
        HttpEntity<CreateEventResource> entity1 = new HttpEntity<>(testCreateResource, headersJSON);

        ResponseEntity<GetEventResource> result1 = this.restTemplate.exchange("/api/events/user/1", HttpMethod.POST, entity1, GetEventResource.class);
        assertEquals(HttpStatus.CREATED, result1.getStatusCode());
        assertEquals(testGetResource, result1.getBody());

        // Test XML
        HttpEntity<CreateEventResource> entity2 = new HttpEntity<>(testCreateResource, headersXML);

        ResponseEntity<GetEventResource> result2 = this.restTemplate.exchange("/api/events/user/1", HttpMethod.POST, entity2, GetEventResource.class);
        assertEquals(HttpStatus.CREATED, result2.getStatusCode());
        assertEquals(testGetResource, result2.getBody());
    }

    /**
     * Tests that a GET request to the /events/user/{userId} endpoint return proper JSON.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testGetEventForUser() throws UserNotFoundException {
        Mockito.when(eventService.getEvents(anyLong()))
                .thenReturn(Collections.singletonList(testGetResource));

        GetEventResource[] expected = {testGetResource};

        // Test JSON
        HttpEntity<String> entity1 = new HttpEntity<>(headersJSON);

        ResponseEntity<GetEventResource[]> result1 = this.restTemplate.exchange("/api/events/user/1", HttpMethod.GET, entity1, GetEventResource[].class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertArrayEquals(expected, result1.getBody());

        // Test XML
        HttpEntity<String> entity2 = new HttpEntity<>(headersXML);

        ResponseEntity<GetEventResource[]> result2 = this.restTemplate.exchange("/api/events/user/1", HttpMethod.GET, entity2, GetEventResource[].class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertArrayEquals(expected, result2.getBody());
    }

    /**
     * Tests that a GET request to the /events/{eventId} endpoint returns the proper JSON.
     * @throws EventNotFoundException Not thrown in this test.
     */
    @Test
    public void testGetEventById() throws EventNotFoundException {
        Mockito.when(eventService.getEvent(anyLong()))
                .thenReturn(testGetResource);

        // Test JSON
        HttpEntity<String> entity1 = new HttpEntity<>(headersJSON);

        ResponseEntity<GetEventResource> result1 = this.restTemplate.exchange("/api/events/1", HttpMethod.GET, entity1, GetEventResource.class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(testGetResource, result1.getBody());

        // Test XML
        HttpEntity<String> entity2 = new HttpEntity<>(headersXML);

        ResponseEntity<GetEventResource> result2 = this.restTemplate.exchange("/api/events/1", HttpMethod.GET, entity2, GetEventResource.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(testGetResource, result2.getBody());
    }

    /**
     * Tests that a POST request to the /events/{eventId} endpoint returns the proper JSON.
     * @throws EventNotFoundException Not thrown in this test.
     */
    @Test
    public void testEditEvent() throws EventNotFoundException {

        Mockito.when(eventService.editEvent(any(), anyLong()))
                .thenReturn(testGetResource);

        // Test JSON
        HttpEntity<CreateEventResource> entity1 = new HttpEntity<>(testCreateResource, headersJSON);

        ResponseEntity<GetEventResource> result1 = this.restTemplate.exchange("/api/events/1", HttpMethod.POST, entity1, GetEventResource.class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(testGetResource, result1.getBody());

        // Test XML
        HttpEntity<CreateEventResource> entity2 = new HttpEntity<>(testCreateResource, headersXML);

        ResponseEntity<GetEventResource> result2 = this.restTemplate.exchange("/api/events/1", HttpMethod.POST, entity2, GetEventResource.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(testGetResource, result2.getBody());
    }

    /**
     * Tests that a DELETE request to the events/{eventId} endpoint calls the correct service method.
     * @throws EventNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteEvent() throws EventNotFoundException {
        HttpEntity entity = new HttpEntity<>(headersXML);
        ResponseEntity result2 = this.restTemplate.exchange("/api/events/1", HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        Mockito.verify(eventService, Mockito.times(1)).deleteEvent(1L);
    }

    /**
     * Tests that a POST request to /events/{eventId}/add returns the proper JSON.
     * @throws EventNotFoundException Not thrown in this test.
     * @throws UserNotFoundException Not thrown in this test.
     * @throws UsernameAndIdMatchException Not thrown in this test.
     */
    @Test
    public void testAddAttendeeToEvent() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException, EventAttendeeLimitReachedException {
        testGetResource.getAttendees().add(new GetUserResource(2L, "testUser", 30L, UserRole.USER));


        Mockito.when(eventService.addEventAttendees(anyLong(), any()))
                .thenReturn(testGetResource);

        GetUserResource[] requestBody = {new GetUserResource(2L, "testUser", 30L, UserRole.USER)};

        // Test JSON
        HttpEntity<GetUserResource[]> entity1 = new HttpEntity<>(requestBody, headersJSON);

        ResponseEntity<GetEventResource> result1 = this.restTemplate.exchange("/api/events/1/add", HttpMethod.POST, entity1, GetEventResource.class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(testGetResource, result1.getBody());

        // Test XML
        HttpEntity<GetUserResource[]> entity2 = new HttpEntity<>(requestBody, headersXML);

        ResponseEntity<GetEventResource> result2 = this.restTemplate.exchange("/api/events/1/add", HttpMethod.POST, entity2, GetEventResource.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(testGetResource, result2.getBody());
    }

    /**
     * Tests that a POST request to /events/{eventId}/remove returns the proper JSON.
     * @throws EventNotFoundException Not thrown in this test.
     * @throws UserNotFoundException Not thrown in this test.
     * @throws UsernameAndIdMatchException Not thrown in this test.
     */
    @Test
    public void testRemoveAttendeeFromEvent() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException {
        Mockito.when(eventService.removeEventAttendees(anyLong(), any()))
                .thenReturn(testGetResource);

        GetUserResource[] requestBody = {new GetUserResource(2L, "testUser", 30L, UserRole.USER)};

        // Test JSON
        HttpEntity<GetUserResource[]> entity1 = new HttpEntity<>(requestBody, headersJSON);

        ResponseEntity<GetEventResource> result1 = this.restTemplate.exchange("/api/events/1/remove", HttpMethod.POST, entity1, GetEventResource.class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(testGetResource, result1.getBody());

        // Test XML
        HttpEntity<GetUserResource[]> entity2 = new HttpEntity<>(requestBody, headersXML);

        ResponseEntity<GetEventResource> result2 = this.restTemplate.exchange("/api/events/1/remove", HttpMethod.POST, entity2, GetEventResource.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(testGetResource, result2.getBody());
    }
}
