package de.webtech.quackr.service.user;

import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.service.user.domain.GetUserResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A mapper to map UserEntity objects to GetUserResource objects.
 */
public class UserMapper {

    /**
     * Maps a single UserEntity to a GetUserResource
     * @param entity The entity to map
     * @return A GetUserResource created from the entity
     */
    public static GetUserResource map(UserEntity entity){
        return new GetUserResource(entity.getId(), entity.getUsername(), entity.getRating());
    }

    /**
     * Maps a list of UserEntity objects to a List of GetUserResource objects.
     * @param entities The list of entities.
     * @return The list of resources created from the entities.
     */
    public static List<GetUserResource> map(Collection<UserEntity> entities){
        List<GetUserResource> users = new ArrayList<>();
        for(UserEntity e : entities){
            users.add(new GetUserResource(e.getId(), e.getUsername(), e.getRating()));
        }
        return users;
    }
}
