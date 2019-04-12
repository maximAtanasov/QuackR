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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     * @return All users saved in the database.
     */
    public Collection<GetUserResource> getUsers() {
        List<UserEntity> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);
        return UserMapper.map(result);
    }

    /**
     *
     * @param id The id of the user.
     * @return The user with the id if such a user exists.
     * @throws UserNotFoundException Thrown if a user with the given id does not exist.
     */
    public GetUserResource getUserById(long id) throws UserNotFoundException {
        Optional<UserEntity> entity = userRepository.findById(id);
        if(entity.isPresent()){
            return UserMapper.map(entity.get());
        }else{
            throw new UserNotFoundException(id);
        }
    }

    /**
     * Creates a new user and saves them in the database.
     * @param resource A CreateUserResource object containing the data needed to create a user.
     * @throws UserWithUsernameAlreadyExistsException Thrown if a user with the same username already exists in the database.
     * @return A GetUserResource object.
     */
    public GetUserResource createUser(CreateUserResource resource) throws UserWithUsernameAlreadyExistsException {
        if(!userRepository.existsByUsername(resource.getUsername())){
            return UserMapper.map(
                    userRepository.save(new UserEntity(resource.getUsername(), resource.getPassword(), resource.getRating()))
            );
        }else{
            throw new UserWithUsernameAlreadyExistsException(resource.getUsername());
        }
    }

    /**
     * Delete a user from the database given it's id.
     * @param userId The id of the user to delete.
     * @throws UserNotFoundException Thrown if a user with the given id is not found.
     */
    public void deleteUser(long userId) throws UserNotFoundException {
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
        }else{
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * Edits the information of an existing user in the database.
     * @param resource A CreateUserResource object containing the needed data.
     * @param userId The id of the user to edit.
     * @throws UserNotFoundException Thrown if such a user is not found.
     * @throws UserWithUsernameAlreadyExistsException Thrown if a user with the same username (if the username has been modified)
     * already exists in the database.
     * @return A GetUserResource object.
     */
    public GetUserResource editUser(CreateUserResource resource, long userId) throws UserNotFoundException, UserWithUsernameAlreadyExistsException {
        if(userRepository.existsById(userId)){
            if(userRepository.existsByUsername(resource.getUsername()) &&
                    userRepository.findByUsername(resource.getUsername()).getId() != userId){
                throw new UserWithUsernameAlreadyExistsException(resource.getUsername());
            }
            UserEntity userEntity = new UserEntity(resource.getUsername(), resource.getPassword(), resource.getRating());
            userEntity.setId(userId);
            return UserMapper.map(
                    userRepository.save(userEntity));
        } else {
            throw new UserNotFoundException(userId);
        }
    }
}
