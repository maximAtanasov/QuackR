package de.webtech.quackr.service.user;

import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.service.AbstractMapper;
import de.webtech.quackr.service.user.resources.GetUserResource;

/**
 * A mapper to map UserEntity objects to GetUserResource objects.
 */
public class UserMapper extends AbstractMapper<GetUserResource, UserEntity> {

    /**
     * Maps a single UserEntity to a GetUserResource
     * @param entity The entity to map
     * @return A GetUserResource created from the entity
     */
    public GetUserResource map(UserEntity entity){
        return new GetUserResource(entity.getId(), entity.getUsername(), entity.getRating(), entity.getRole());
    }
}
