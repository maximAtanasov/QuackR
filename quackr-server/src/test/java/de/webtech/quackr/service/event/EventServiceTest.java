package de.webtech.quackr.service.event;

import de.webtech.quackr.persistance.event.EventEntity;
import de.webtech.quackr.persistance.event.EventRepository;
import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.persistance.user.UserRepository;
import de.webtech.quackr.service.ServiceTestTemplate;
import de.webtech.quackr.service.event.resources.CreateEventResource;
import de.webtech.quackr.service.event.resources.GetEventResource;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.UserService;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class EventServiceTest extends ServiceTestTemplate {

    @MockBean
    UserRepository userRepository;

    @MockBean
    EventRepository eventRepository;

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    /**
     * Mocks all methods from the userRepository and eventRepository that need mocking.
     * Returns example/dummy data where required.
     */
    @Before
    public void setUp() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(new UserEntity("testUser", "testPassword", 0L)));

        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(new UserEntity("testUser", "testPassword", 0L),
                        new UserEntity("testUser2", "testPassword2", 50L)));

        Mockito.when(userRepository.existsById(any()))
                .thenReturn(true);

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
        Mockito.when(eventRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        Mockito.when(eventRepository.findAll()).thenReturn(Collections.singletonList(entity));

        Mockito.when(eventRepository.existsById(any()))
                .thenReturn(true);
    }

    /**
     * Tests the getEventById() method of the service.
     * @throws EventNotFoundException Doesn't throw in this test.
     */
    @Test
    public void testGetEventById() throws EventNotFoundException {
        GetEventResource result = eventService.getEvent(1L);
        Mockito.verify(eventRepository, Mockito.times(1)).findById(anyLong());
        Assert.assertEquals("BBQ", result.getTitle());
    }

    /**
     * Tests the getEvents() method of the service.
     * @throws UserNotFoundException Not thrown in this test
     */
    @Test
    public void testGetEvents() throws UserNotFoundException {
        Collection<GetEventResource> result = eventService.getEvents(1L);

        Assert.assertEquals(1, result.size());
        Iterator<GetEventResource> it = result.iterator();
        Assert.assertEquals("BBQ", it.next().getTitle());
    }

    /**
     * Tests the createEvent() method of the service.
     * @throws UserNotFoundException Doesn't throw in this test.
     */
    @Test
    public void testCreateEvent() throws UserNotFoundException {
        CreateEventResource resource = new CreateEventResource();
        resource.setTitle("BBQ1");
        resource.setDescription("BBQ1 at Stan");
        resource.setDate(new Date());
        resource.setLocation("Somewhere");
        resource.setPublic(false);
        resource.setAttendeeLimit(20L);

        GetEventResource result = eventService.createEvent(resource, 1L);
        Mockito.verify(userRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(eventRepository, Mockito.times(1)).save(any());
        Assert.assertEquals(resource.getTitle(), result.getTitle());
        Assert.assertEquals(resource.getDescription(), result.getDescription());
        Assert.assertEquals(resource.getAttendeeLimit(), result.getAttendeeLimit());
        Assert.assertEquals(resource.getDate(), result.getDate());
        Assert.assertEquals(resource.getLocation(), result.getLocation());
    }

    /**
     * Tests the editEvent() method of the service.
     * @throws EventNotFoundException Not thrown in this test.
     */
    @Test
    public void testEditEvent() throws EventNotFoundException {
        CreateEventResource resource = new CreateEventResource();
        resource.setTitle("BBQ1");
        resource.setDescription("BBQ1 at Stan");
        resource.setDate(new Date());
        resource.setLocation("Somewhere");
        resource.setPublic(false);
        resource.setAttendeeLimit(20L);

        GetEventResource result = eventService.editEvent(resource, 1L);
        Mockito.verify(eventRepository, Mockito.times(1)).findById(any());
        Mockito.verify(eventRepository, Mockito.times(1)).save(any());

        Assert.assertEquals(resource.getTitle(), result.getTitle());
        Assert.assertEquals(resource.getDescription(), result.getDescription());
        Assert.assertEquals(resource.getAttendeeLimit(), result.getAttendeeLimit());
        Assert.assertEquals(resource.getDate(), result.getDate());
        Assert.assertEquals(resource.getLocation(), result.getLocation());
    }

    /**
     * Tests the deleteEvent() method of the service.
     * @throws EventNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteEvent() throws EventNotFoundException {
        eventService.deleteEvent(1L);
        Mockito.verify(eventRepository, Mockito.times(1)).existsById(any());
        Mockito.verify(eventRepository, Mockito.times(1)).deleteById(1L);
    }

    /**
     * Tests the addEventAttendees() method of the service.
     * @throws EventNotFoundException Not thrown in this test.
     */
    @Test
    public void testAddAttendees() throws EventNotFoundException, UserNotFoundException {
        eventService.addEventAttendees(1L, Collections.singletonList(new GetUserResource(1L, "testUser", 3L)));

        Mockito.verify(eventRepository, Mockito.times(1)).findById(any());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(eventRepository, Mockito.times(1)).save(any());
    }
}
