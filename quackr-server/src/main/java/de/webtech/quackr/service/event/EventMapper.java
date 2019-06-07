package de.webtech.quackr.service.event;

import de.webtech.quackr.persistence.event.EventEntity;
import de.webtech.quackr.service.AbstractMapper;
import de.webtech.quackr.service.comment.CommentMapper;
import de.webtech.quackr.service.event.resources.GetEventResource;
import de.webtech.quackr.service.user.UserMapper;

import java.util.ArrayList;

/**
 * A mapper to map EventEntity objects to GetEventResource objects.
 */
class EventMapper extends AbstractMapper<GetEventResource, EventEntity> {

    private final CommentMapper commentMapper = new CommentMapper();

    private final UserMapper userMapper = new UserMapper();

    /**
     * Maps a single EventEntity to a GetEventResource
     * @param entity The entity to map
     * @return A GetEventResource created from the entity
     */
    public GetEventResource map(EventEntity entity){
        GetEventResource resource = new GetEventResource();
        resource.setId(entity.getId());
        resource.setDate(entity.getDate());
        resource.setAttendeeLimit(entity.getAttendeeLimit());

        if(entity.getAttendees() != null){
            resource.setAttendees(userMapper.map(entity.getAttendees()));
        }else{
            resource.setAttendees(new ArrayList<>());
        }

        if(entity.getComments() != null){
            resource.setComments(commentMapper.map(entity.getComments()));
        }else{
            resource.setComments(new ArrayList<>());
        }

        resource.setDescription(entity.getDescription());
        resource.setTitle(entity.getTitle());
        resource.setLocation(entity.getLocation());
        resource.setPublic(entity.isPublic());
        resource.setOrganizerId(entity.getOrganizer().getId());
        return resource;
    }
}
