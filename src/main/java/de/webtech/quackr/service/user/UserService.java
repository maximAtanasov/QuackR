package de.webtech.quackr.service.user;

import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.persistance.user.UserRepository;
import de.webtech.quackr.service.user.domain.CreateUserResource;
import de.webtech.quackr.service.user.domain.GetUserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
    }

    public Collection<GetUserResource> getUsers() {
        List<UserEntity> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);
        return userMapper.map(result);
    }

    public GetUserResource getUserById(long id) {
        Optional<UserEntity> entity = userRepository.findById(id);
        return entity.map(userMapper::map).orElse(null);
    }

    public void createUser(CreateUserResource resource) throws UserAlreadyExistsException {
        if(userRepository.findUserEntityByUsername(resource.getUsername()) == null){
            userRepository.save(new UserEntity(resource.getUsername(), resource.getPassword()));
        } else {
            throw new UserAlreadyExistsException();
        }
    }
}
