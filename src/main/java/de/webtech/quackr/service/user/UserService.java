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

    public GetUserResource getUserById(long id) throws UserNotFoundException {
        Optional<UserEntity> entity = userRepository.findById(id);
        if(entity.isPresent()){
            return userMapper.map(entity.get());
        }else{
            throw new UserNotFoundException();
        }
    }

    public void createUser(CreateUserResource resource) throws UsernameAlreadyExistsException {
        if(!userRepository.existsByUsername(resource.getUsername())){
            userRepository.save(new UserEntity(resource.getUsername(), resource.getPassword(), resource.getRating()));
        }else{
            throw new UsernameAlreadyExistsException();
        }
    }

    public void deleteUser(long userId) throws UserNotFoundException {
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
        }else{
            throw new UserNotFoundException();
        }
    }

    public void editUser(CreateUserResource resource, long userId) throws UserNotFoundException, UsernameAlreadyExistsException {
        if(userRepository.existsById(userId)){
            if(userRepository.findByUsername(resource.getUsername()).getId() != userId){
                throw new UsernameAlreadyExistsException();
            }
            UserEntity userEntity = new UserEntity(resource.getUsername(), resource.getPassword(), resource.getRating());
            userEntity.setId(userId);
            userRepository.save(userEntity);
        } else {
            throw new UserNotFoundException();
        }
    }
}
