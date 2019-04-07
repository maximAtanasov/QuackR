package de.webtech.quackr.service.user;

import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.service.user.domain.GetUserResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserMapper {

    public GetUserResource map(UserEntity entity){
        return new GetUserResource(entity.getId(), entity.getUsername(), entity.getRating());
    }

    public List<GetUserResource> map(Collection<UserEntity> entities){
        List<GetUserResource> users = new ArrayList<>();
        for(UserEntity e : entities){
            users.add(new GetUserResource(e.getId(), e.getUsername(), e.getRating()));
        }
        return users;
    }
}
