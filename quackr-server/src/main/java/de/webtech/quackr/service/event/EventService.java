package de.webtech.quackr.service.event;

import de.webtech.quackr.persistence.event.EventEntity;
import de.webtech.quackr.persistence.event.EventRepository;
import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRepository;
import de.webtech.quackr.service.event.resources.CreateEventResource;
import de.webtech.quackr.service.event.resources.GetEventResource;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EventService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final EventMapper eventMapper = new EventMapper();

    @Autowired
    EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /**
     * Return all events for the selected user.
     * @param userId The id of the user.
     * @return All events for the selected user.
     * @throws UserNotFoundException Thrown when the selected user is not found.
     */
    public Collection<GetEventResource> getEvents(long userId) throws UserNotFoundException {
        if(userRepository.existsById(userId)){
            return eventMapper.map(eventRepository.findByOrganizerId(userId));
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * Creates an event.
     * @param resource A CreateEventResource object.
     * @param userId The userId of the event organizer.
     * @return A GetEventResource object.
     * @throws UserNotFoundException Thrown if the user with userId is not found.
     */
    public GetEventResource createEvent(CreateEventResource resource, long userId) throws UserNotFoundException {
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isPresent()){
            EventEntity newEvent = new EventEntity();
            newEvent.setTitle(resource.getTitle());
            newEvent.setDescription(resource.getDescription());
            newEvent.setDate(resource.getDate());
            newEvent.setAttendeeLimit(resource.getAttendeeLimit());
            newEvent.setLocation(resource.getLocation());
            newEvent.setPublic(resource.isPublic());
            newEvent.setOrganizer(user.get());
            eventRepository.save(newEvent);
            return eventMapper.map(newEvent);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * Delete an event from the database.
     * @param eventId The id of the event to delete.
     * @throws EventNotFoundException Thrown if the event is not found.
     */
    public void deleteEvent(long eventId) throws EventNotFoundException {
        Optional<EventEntity> event = eventRepository.findById(eventId);
        if(event.isPresent()){
            eventRepository.delete(event.get());
        }else{
            throw new EventNotFoundException(eventId);
        }
    }

    /**
     * Adds attendees for an event.
     * @param eventId The id of the event.
     * @param users The attendees.
     * @return A GetEventResource object.
     * @throws EventNotFoundException Thrown if the event is not found.
     * @throws UserNotFoundException Thrown if any user in users is not found.
     * @throws UsernameAndIdMatchException Thrown if a username does not match with an id.
     */
    public GetEventResource addEventAttendees(long eventId, Collection<GetUserResource> users)
            throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException, EventAttendeeLimitReachedException {
        Optional<EventEntity> event = eventRepository.findById(eventId);
        if(event.isPresent()){
            if(event.get().getAttendees().size() == event.get().getAttendeeLimit()){
                throw new EventAttendeeLimitReachedException();
            }
            Collection<UserEntity> userEntities = getEntitiesForResources(users);
            userEntities.forEach(u -> {
                if(!event.get().getAttendees().contains(u)){
                    event.get().getAttendees().add(u);
                }
            });
            eventRepository.save(event.get());
            return eventMapper.map(event.get());
        }else{
            throw new EventNotFoundException(eventId);
        }
    }

    /**
     * Removes attendees from an event.
     * @param eventId The id of the event.
     * @param users The attendees.
     * @return A GetEventResource object.
     * @throws EventNotFoundException Thrown if the event is not found.
     * @throws UserNotFoundException Thrown if any user in users is not found.
     * @throws UsernameAndIdMatchException Thrown if a username does not match with an id.
     */
    public GetEventResource removeEventAttendees(long eventId, Collection<GetUserResource> users)
            throws EventNotFoundException, UserNotFoundException, UsernameAndIdMatchException {
        Optional<EventEntity> event = eventRepository.findById(eventId);
        if(event.isPresent()){
            Collection<UserEntity> userEntities = getEntitiesForResources(users);
            event.get().getAttendees().removeAll(userEntities);
            eventRepository.save(event.get());
            return eventMapper.map(event.get());
        }else{
            throw new EventNotFoundException(eventId);
        }
    }

    /**
     * Returns a collection of user entities corresponding to the given resources
     * @param resources A Collection of GetUserResource objects.
     * @return A collection of user entities corresponding to the given resources
     * @throws UserNotFoundException Thrown if any of the users within resources is not found.
     * @throws UsernameAndIdMatchException Thrown if a username does not match with an id.
     */
    private Collection<UserEntity> getEntitiesForResources(Collection<GetUserResource> resources) throws UserNotFoundException, UsernameAndIdMatchException {
        Collection<UserEntity> userEntities = new ArrayList<>();
        for(GetUserResource user : resources){
            Optional<UserEntity> userEntity = userRepository.findById(user.getId());
            if(userEntity.isPresent()){
                if(userEntity.get().getUsername().equals(user.getUsername())) {
                    userEntities.add(userEntity.get());
                } else {
                    throw new UsernameAndIdMatchException(user.getId(), user.getUsername());
                }
            }else{
                throw new UserNotFoundException(user.getId());
            }
        }
        return userEntities;
    }

    /**
     * Retrieves a single event from the database.
     * @param eventId The id of the event.
     * @return A GetEventResource object.
     * @throws EventNotFoundException Thrown if the event is not found.
     */
    public GetEventResource getEvent(long eventId) throws EventNotFoundException {
        Optional<EventEntity> eventEntity = eventRepository.findById(eventId);
        if(eventEntity.isPresent()){
            return eventMapper.map(eventEntity.get());
        }else{
            throw new EventNotFoundException(eventId);
        }
    }

    /**
     * Edits an existing event in the database.
     * @param resource A CreateEventResource containing the new data.
     * @param eventId The id of the event to change.
     * @return A GetEventResource object.
     * @throws EventNotFoundException Thrown if the event is not found.
     */
    public GetEventResource editEvent(CreateEventResource resource, long eventId) throws EventNotFoundException {
        Optional<EventEntity> eventEntity = eventRepository.findById(eventId);
        if(eventEntity.isPresent()){
            eventEntity.get().setTitle(resource.getTitle());
            eventEntity.get().setPublic(resource.isPublic());
            eventEntity.get().setLocation(resource.getLocation());
            eventEntity.get().setDate(resource.getDate());
            eventEntity.get().setAttendeeLimit(resource.getAttendeeLimit());
            eventEntity.get().setDescription(resource.getDescription());
            eventEntity.get().setPublic(resource.isPublic());
            eventRepository.save(eventEntity.get());
            return eventMapper.map(eventEntity.get());
        }else{
            throw new EventNotFoundException(eventId);
        }
    }
}
