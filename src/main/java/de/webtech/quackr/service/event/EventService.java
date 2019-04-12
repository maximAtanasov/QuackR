package de.webtech.quackr.service.event;

import de.webtech.quackr.persistance.event.EventEntity;
import de.webtech.quackr.persistance.event.EventRepository;
import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.persistance.user.UserRepository;
import de.webtech.quackr.service.event.domain.CreateEventResource;
import de.webtech.quackr.service.event.domain.GetEventResource;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.domain.GetUserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository) {
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
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isPresent()){
            return EventMapper.map(eventRepository.findByOrganizerId(userId));
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
            userRepository.save(user.get());
            return EventMapper.map(newEvent);
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
        if(eventRepository.existsById(eventId)){
            eventRepository.deleteById(eventId);
        }else{
            throw new EventNotFoundException(eventId);
        }
    }

    /**
     * Sets the attendees for an event.
     * @param eventId The id of the event.
     * @param users The attendees.
     * @return A GetEventResource object.
     * @throws EventNotFoundException Thrown if the event is not found.
     * @throws UserNotFoundException Thrown if any user in users is not found.
     */
    public GetEventResource setEventAttendees(long eventId, Collection<GetUserResource> users) throws EventNotFoundException, UserNotFoundException {
        Optional<EventEntity> event = eventRepository.findById(eventId);
        if(event.isPresent()){
            List<UserEntity> userEntities = new ArrayList<>();
            for(GetUserResource user : users){
                Optional<UserEntity> userEntity = userRepository.findById(user.getId());
                if(userEntity.isPresent()){
                    userEntities.add(userEntity.get());
                }else{
                    throw new UserNotFoundException(user.getId());
                }
            }
            event.get().setAttendees(userEntities);
            return EventMapper.map(event.get());
        }else{
            throw new EventNotFoundException(eventId);
        }
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
            return EventMapper.map(eventEntity.get());
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
            eventRepository.save(eventEntity.get());
            return EventMapper.map(eventEntity.get());
        }else{
            throw new EventNotFoundException(eventId);
        }
    }
}
