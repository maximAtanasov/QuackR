package de.webtech.quackr.service.user;

import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.service.user.domain.UserResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserMapper {

    public UserResource map(UserEntity entity){
        return new UserResource(entity.getId(), entity.getUsername());
    }

    public List<UserResource> map(Collection<UserEntity> entities){
        List<UserResource> users = new ArrayList<>();
        for(UserEntity e : entities){
            users.add(new UserResource(e.getId(), e.getUsername()));
        }
        return users;
    }
}
