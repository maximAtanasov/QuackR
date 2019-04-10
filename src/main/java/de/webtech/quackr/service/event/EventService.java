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

    public GetEventResource createEvent(CreateEventResource resource, long userId) throws UserNotFoundException {
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isPresent()){
            EventEntity newEvent = new EventEntity();
            newEvent.setTitle(resource.getTitle());
            newEvent.setDescription(resource.getDescription());
            newEvent.setDate(resource.getDate());
            newEvent.setAttendeeLimit(resource.getAttendeeLimit());
            newEvent.setLocation(resource.getLocation());
            user.get().getEvents().add(newEvent);
            eventRepository.save(newEvent);
            userRepository.save(user.get());
            return EventMapper.map(newEvent);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    public void deleteEvent(long eventId) throws EventNotFoundException {
        if(eventRepository.existsById(eventId)){
            eventRepository.deleteById(eventId);
        }else{
            throw new EventNotFoundException();
        }
    }

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
            throw new EventNotFoundException();
        }
    }
}
