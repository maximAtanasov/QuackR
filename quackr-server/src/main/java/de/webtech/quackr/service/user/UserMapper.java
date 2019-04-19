package de.webtech.quackr.service.user;

import de.webtech.quackr.AbstractMapper;
import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.service.user.domain.GetUserResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        return new GetUserResource(entity.getId(), entity.getUsername(), entity.getRating());
    }
}
