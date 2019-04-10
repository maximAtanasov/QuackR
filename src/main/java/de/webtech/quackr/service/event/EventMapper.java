package de.webtech.quackr.service.event;

import de.webtech.quackr.persistance.event.EventEntity;
import de.webtech.quackr.service.event.domain.GetEventResource;
import de.webtech.quackr.service.user.UserMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A mapper to map EventEntity objects to GetEventResource objects.
 */
public class EventMapper {

    /**
     * Maps a single EventEntity to a GetEventResource
     * @param entity The entity to map
     * @return A GetEventResource created from the entity
     */
    static GetEventResource map(EventEntity entity){
        GetEventResource resource = new GetEventResource();
        resource.setId(entity.getId());
        resource.setDate(entity.getDate());
        resource.setAttendeeLimit(entity.getAttendeeLimit());
        resource.setAttendees(UserMapper.map(entity.getAttendees()));
        resource.setDescription(entity.getDescription());
        resource.setTitle(entity.getTitle());
        resource.setLocation(entity.getLocation());
        return resource;
    }

    /**
     * Maps a list of EventEntity objects to a List of GetEventResource objects.
     * @param entities The list of entities.
     * @return The list of resources created from the entities.
     */
    static List<GetEventResource> map(Collection<EventEntity> entities){
        List<GetEventResource> events = new ArrayList<>();
        for(EventEntity e : entities){
            events.add(EventMapper.map(e));
        }
        return events;
    }
}
