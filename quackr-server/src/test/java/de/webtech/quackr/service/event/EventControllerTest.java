package de.webtech.quackr.service.event;

import de.webtech.quackr.service.event.resources.CreateEventResource;
import de.webtech.quackr.service.event.resources.GetEventResource;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerTest {

    @MockBean
    private EventService eventService;

    @Autowired
    private TestRestTemplate restTemplate;

    private CreateEventResource testCreateResource;
    private GetEventResource testGetResource;

    @Before
    public void setUp() {

        testGetResource = new GetEventResource();
        testGetResource.setAttendeeLimit(20L);
        testGetResource.setDate(new Date());
        testGetResource.setDescription("something");
        testGetResource.setLocation("Somewhere");
        testGetResource.setPublic(true);
        testGetResource.setTitle("BBQ");
        testGetResource.setId(1L);

        testCreateResource = new CreateEventResource();
        testCreateResource.setAttendeeLimit(20L);
        testCreateResource.setDate(new Date());
        testCreateResource.setDescription("something");
        testCreateResource.setLocation("Somewhere");
        testCreateResource.setPublic(true);
        testCreateResource.setTitle("BBQ");
        testGetResource.setAttendees(new ArrayList<>());
    }

    @Test
    public void testCreateEvent() throws UserNotFoundException {

        Mockito.when(eventService.createEvent(any(), anyLong()))
                .thenReturn(testGetResource);

        ResponseEntity<GetEventResource> entity = this.restTemplate.postForEntity("/events/user/1", testCreateResource, GetEventResource.class);
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(testGetResource , entity.getBody());
    }

    @Test
    public void testGetEventForUser() throws UserNotFoundException {

        Mockito.when(eventService.getEvents(anyLong()))
                .thenReturn(Collections.singletonList(testGetResource));

        ResponseEntity<GetEventResource[]> entity = this.restTemplate.getForEntity("/events/user/1", GetEventResource[].class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());

        GetEventResource[] expected = {testGetResource};
        assertArrayEquals(expected, entity.getBody());
    }


    @Test
    public void testGetEventById() throws EventNotFoundException {



        Mockito.when(eventService.getEvent(anyLong()))
                .thenReturn(testGetResource);

        ResponseEntity<GetEventResource> entity = this.restTemplate.getForEntity("/events/1", GetEventResource.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(testGetResource, entity.getBody());
    }

    @Test
    public void testEditEvent() throws EventNotFoundException {

        Mockito.when(eventService.editEvent(any(), anyLong()))
                .thenReturn(testGetResource);

        ResponseEntity<GetEventResource> entity = this.restTemplate.postForEntity("/events/1", testCreateResource, GetEventResource.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(testGetResource, entity.getBody());
    }

    @Test
    public void testDeleteEvent() throws EventNotFoundException {
        this.restTemplate.delete("/events/1");
        Mockito.verify(eventService, Mockito.times(1)).deleteEvent(1L);
    }

    @Test
    public void testAddAttendeeToEvent() throws EventNotFoundException, UserNotFoundException {
        testGetResource.getAttendees().add(new GetUserResource(2L, "testUser", 30L));


        Mockito.when(eventService.addEventAttendees(anyLong(), any()))
                .thenReturn(testGetResource);

        ResponseEntity<GetEventResource> entity =
                this.restTemplate.postForEntity("/events/1/add",
                        Collections.singletonList(new GetUserResource(2L, "testUser", 30L)), GetEventResource.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(testGetResource, entity.getBody());
    }

    @Test
    public void testRemoveAttendeeFromEvent() throws EventNotFoundException, UserNotFoundException {
        Mockito.when(eventService.removeEventAttendees(anyLong(), any()))
                .thenReturn(testGetResource);

        ResponseEntity<GetEventResource> entity =
                this.restTemplate.postForEntity("/events/1/remove",
                        Collections.singletonList(new GetUserResource(2L, "testUser", 30L)), GetEventResource.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(testGetResource, entity.getBody());
    }
}
