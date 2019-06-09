package de.webtech.quackr.service.event;

import de.webtech.quackr.persistence.event.EventEntity;
import de.webtech.quackr.persistence.event.EventRepository;
import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRepository;
import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.event.resources.CreateEventResource;
import de.webtech.quackr.service.event.resources.GetEventResource;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

public class EventServiceTest {

    private UserRepository userRepository = mock(UserRepository.class);

    private EventRepository eventRepository = mock(EventRepository.class);

    private EventService eventService;

    /**
     * Mocks all methods from the userRepository and eventRepository that need mocking.
     * Returns example/dummy data where required.
     */
    @Before
    public void setUp() {
        eventService = new EventService(eventRepository, userRepository);
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(new UserEntity("testUser", "testPassword", 0L, UserRole.USER)));

        Mockito.when(userRepository.findById(8L))
                .thenReturn(Optional.of(new UserEntity("testUser", "testPassword", 0L, UserRole.USER)));

        Mockito.when(userRepository.findById(7L))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(new UserEntity("testUser", "testPassword", 0L, UserRole.USER),
                        new UserEntity("testUser2", "testPassword2", 50L, UserRole.USER)));

        Mockito.when(userRepository.existsById(1L))
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
        entity.setId(2L);

        Mockito.when(eventRepository.findByOrganizerId(anyLong())).thenReturn(Collections.singletonList(entity));
        Mockito.when(eventRepository.findById(2L)).thenReturn(Optional.of(entity));
        Mockito.when(eventRepository.findById(7L)).thenReturn(Optional.empty());
        Mockito.when(eventRepository.findAll()).thenReturn(Collections.singletonList(entity));

        Mockito.when(eventRepository.existsById(2L))
                .thenReturn(true);

        Mockito.when(eventRepository.existsById(7L))
                .thenReturn(false);
    }

    /**
     * Tests the getEventById() method of the service.
     * @throws EventNotFoundException Doesn't throw in this test.
     */
    @Test
    public void testGetEventById() throws EventNotFoundException {
        GetEventResource result = eventService.getEvent(2L);
        Assert.assertEquals("BBQ", result.getTitle());
    }

    /**
     * Tests that the getEventById() method of the service throws an exception if the event is not found.
     * @throws EventNotFoundException Doesn't throw in this test.
     */
    @Test(expected = EventNotFoundException.class)
    public void testGetEventByIdThrowsExceptionIfEventNotFound() throws EventNotFoundException {
        eventService.getEvent(7L);
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
     * Tests that the getEvents() method of the service throws an Exception
     * if the user is not found.
     * @throws UserNotFoundException Checked in this test.
     */
    @Test(expected = UserNotFoundException.class)
    public void testGetEventsThrowsExceptionIfUserNotFound() throws UserNotFoundException {
        eventService.getEvents(7L);
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
        resource.setAttendeeLimit(20);

        GetEventResource result = eventService.createEvent(resource, 1L);
        Mockito.verify(eventRepository, Mockito.times(1)).save(any());
        Assert.assertEquals(resource.getTitle(), result.getTitle());
        Assert.assertEquals(resource.getDescription(), result.getDescription());
        Assert.assertEquals(resource.getAttendeeLimit(), result.getAttendeeLimit());
        Assert.assertEquals(resource.getDate(), result.getDate());
        Assert.assertEquals(resource.getLocation(), result.getLocation());
    }

    /**
     * Tests that the createEvent() method of the service
     * throws an exception if the user is not found.
     * @throws UserNotFoundException Checked in this test.
     */
    @Test(expected = UserNotFoundException.class)
    public void testCreateEventThrowsExceptionIfUserNotFound() throws UserNotFoundException {
        CreateEventResource resource = new CreateEventResource();

        eventService.createEvent(resource, 7L);
        Mockito.verify(eventRepository, Mockito.times(0)).save(any());
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
        resource.setAttendeeLimit(20);

        GetEventResource result = eventService.editEvent(resource, 2L);
        Mockito.verify(eventRepository, Mockito.times(1)).save(any());

        Assert.assertEquals(resource.getTitle(), result.getTitle());
        Assert.assertEquals(resource.getDescription(), result.getDescription());
        Assert.assertEquals(resource.getAttendeeLimit(), result.getAttendeeLimit());
        Assert.assertEquals(resource.getDate(), result.getDate());
        Assert.assertEquals(resource.getLocation(), result.getLocation());
    }

    /**
     * Tests that the editEvent() method of the service throws
     * an exception in the event is not found.
     * @throws EventNotFoundException Checked in this test.
     */
    @Test(expected = EventNotFoundException.class)
    public void testEditEventThrowsExceptionIfEventNotFound() throws EventNotFoundException {
        CreateEventResource resource = new CreateEventResource();
        eventService.editEvent(resource, 7L);
        Mockito.verify(eventRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests the deleteEvent() method of the service.
     * @throws EventNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteEvent() throws EventNotFoundException {
        eventService.deleteEvent(2L);
        Mockito.verify(eventRepository, Mockito.times(1)).delete(any());
    }

    /**
     * Tests that the deleteEvent() method of the service throws an
     * exception if the event is not found.
     * @throws EventNotFoundException Checked in this test.
     */
    @Test(expected = EventNotFoundException.class)
    public void testDeleteEventThrowsExceptionIfEventNotFound() throws EventNotFoundException {
        eventService.deleteEvent(7L);
        Mockito.verify(eventRepository, Mockito.times(0)).delete(any());
    }

    /**
     * Tests the addEventAttendees() method of the service.
     * @throws EventNotFoundException Not thrown in this test.
     * @throws UserNotFoundException Not thrown in this test.
     * @throws UsernameAndIdMatchException Not thrown in this test.
     */
    @Test
    public void testAddAttendees() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException, EventAttendeeLimitReachedException {
        GetEventResource result = eventService.addEventAttendees(2L,
                Collections.singletonList(new GetUserResource(1L, "testUser", 3L, UserRole.USER)));
        Assert.assertEquals(1L, result.getAttendees().size());
        Assert.assertEquals("testUser", result.getAttendees().iterator().next().getUsername());
        Mockito.verify(eventRepository, Mockito.times(1)).save(any());
    }

    /**
     * Tests that the addEventAttendees() method of the service
     * throws an exception if the event is not found.
     * @throws EventNotFoundException Checked in this test.
     * @throws UserNotFoundException Not thrown in this test.
     * @throws UsernameAndIdMatchException Not thrown in this test.
     */
    @Test(expected = EventNotFoundException.class)
    public void testAddAttendeesThrowsExceptionIfEventNotFound() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException, EventAttendeeLimitReachedException {
        eventService.addEventAttendees(7L,
                Collections.singletonList(new GetUserResource(1L, "testUser", 3L, UserRole.USER)));
        Mockito.verify(eventRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests that the addEventAttendees() method of the service
     * throws an exception if the user is not found.
     * @throws EventNotFoundException Not thrown in this test.
     * @throws UserNotFoundException Checked in this test.
     * @throws UsernameAndIdMatchException Not thrown in this test.
     */
    @Test(expected = UserNotFoundException.class)
    public void testAddAttendeesThrowsExceptionIfUserNotFound() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException, EventAttendeeLimitReachedException {
        eventService.addEventAttendees(2L,
                Collections.singletonList(new GetUserResource(7L, "testUser", 3L, UserRole.USER)));
        Mockito.verify(eventRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests that the addEventAttendees() method of the service
     * throws an exception if the username and id do not match.
     * @throws EventNotFoundException Not thrown in this test.
     * @throws UserNotFoundException Not thrown in this test.
     * @throws UsernameAndIdMatchException Checked in this test.
     */
    @Test(expected = UsernameAndIdMatchException.class)
    public void testAddAttendeesThrowsExceptionIfUsernameAndIdDoNotMatch() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException, EventAttendeeLimitReachedException {
        eventService.addEventAttendees(2L,
                Collections.singletonList(new GetUserResource(8L, "testUser3", 3L, UserRole.USER)));
        Mockito.verify(eventRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests the removeEventAttendees() method of the service.
     * @throws EventNotFoundException Not thrown in this test.
     * @throws UserNotFoundException Not thrown in this test.
     * @throws UsernameAndIdMatchException Not thrown in this test.
     */
    @Test
    public void testRemoveAttendees() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException {
        GetEventResource result = eventService.removeEventAttendees(2L,
                Collections.singletonList(new GetUserResource(1L, "testUser", 3L, UserRole.USER)));
        Assert.assertTrue(result.getAttendees().isEmpty());
        Mockito.verify(eventRepository, Mockito.times(1)).save(any());
    }

    /**
     * Tests that the removeEventAttendees() method of the service
     * throws an exception if the event is not found.
     * @throws EventNotFoundException Checked in this test.
     * @throws UserNotFoundException Not thrown in this test.
     * @throws UsernameAndIdMatchException Not thrown in this test.
     */
    @Test(expected = EventNotFoundException.class)
    public void testRemoveAttendeesThrowsExceptionIfEventNotFound() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException {
        eventService.removeEventAttendees(7L,
                Collections.singletonList(new GetUserResource(1L, "testUser", 3L, UserRole.USER)));
        Mockito.verify(eventRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests that the removeEventAttendees() method of the service
     * throws an exception if the user is not found.
     * @throws EventNotFoundException Not thrown in this test.
     * @throws UserNotFoundException Checked in this test.
     * @throws UsernameAndIdMatchException Not thrown in this test.
     */
    @Test(expected = UserNotFoundException.class)
    public void testRemoveAttendeesThrowsExceptionIfUserNotFound() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException {
        eventService.removeEventAttendees(2L,
                Collections.singletonList(new GetUserResource(7L, "testUser", 3L, UserRole.USER)));
        Mockito.verify(eventRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests that the removeEventAttendees() method of the service
     * throws an exception if the user is not found.
     * @throws EventNotFoundException Not thrown in this test.
     * @throws UserNotFoundException Not thrown in this test.
     * @throws UsernameAndIdMatchException Checked in this test.
     */
    @Test(expected = UsernameAndIdMatchException.class)
    public void testRemoveAttendeesThrowsExceptionIfUsernameAndIdDoNotMatch() throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException {
        eventService.removeEventAttendees(2L,
                Collections.singletonList(new GetUserResource(8L, "testUser3", 3L, UserRole.USER)));
        Mockito.verify(eventRepository, Mockito.times(0)).save(any());
    }
}
