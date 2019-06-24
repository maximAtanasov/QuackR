package de.webtech.quackr.service.user;

import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRepository;
import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.authentication.JWTToken;
import de.webtech.quackr.service.authentication.TokenUtil;
import de.webtech.quackr.service.user.resources.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     * @return All users saved in the database.
     */
    public Collection<GetUserResource> getUsers() {
        List<UserEntity> result = new ArrayList<>(userRepository.findAll());
        return userMapper.map(result);
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
            return userMapper.map(entity.get());
        }else{
            throw new UserNotFoundException(id);
        }
    }

    /**
     * Creates a new user and saves them in the database.
     * If no other admins are registered, the one being registered is made an admin.
     * @param resource A CreateUserResource object containing the data needed to create a user.
     * @throws UserWithUsernameAlreadyExistsException Thrown if a user with the same username already exists in the database.
     * @return A GetUserResource object.
     */
    public GetUserResource createUser(CreateUserResource resource) throws UserWithUsernameAlreadyExistsException {
        if(!userRepository.existsByUsername(resource.getUsername())){

            //If there are no other admins registered, make this user one
            UserRole role = UserRole.USER;
            if(userRepository.findByRole(UserRole.ADMIN).isEmpty()){
                role = UserRole.ADMIN;
            }
            UserEntity userEntity = new UserEntity(resource.getUsername(),
                    BCrypt.hashpw(resource.getPassword(), BCrypt.gensalt()),
                    resource.getRating(), role);

            userRepository.save(userEntity);
            return userMapper.map(userEntity);
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
        Optional<UserEntity> entity = userRepository.findById(userId);
        if(entity.isPresent()){
            userRepository.delete(entity.get());
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
    public GetUserResource editUser(EditUserResource resource, long userId) throws UserNotFoundException, UserWithUsernameAlreadyExistsException {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if(userEntity.isPresent()){
            if(userRepository.existsByUsername(resource.getUsername()) &&
                    userRepository.findByUsername(resource.getUsername()).getId() != userId){
                throw new UserWithUsernameAlreadyExistsException(resource.getUsername());
            }
            userEntity.get().setUsername(resource.getUsername());
            userEntity.get().setRating(resource.getRating());
            userEntity.get().setRole(resource.getRole());
            userRepository.save(userEntity.get());

            //Do not rehash the password if it has not changed
            if(resource.getPassword() != null && !BCrypt.checkpw(resource.getPassword(), userEntity.get().getPassword())){
                userEntity.get().setPassword(BCrypt.hashpw(resource.getPassword(), BCrypt.gensalt()));
            }
            return userMapper.map(userEntity.get());
        } else {
            throw new UserNotFoundException(userId);
        }
    }


    /**
     * Checks the user's credentials and returns a JWT.
     * @param resource A LoginUserResource containing the required credentials.
     * @return A JSON Web Token corresponding to the credentials.
     * @throws UserNotFoundException Thrown if a user with the given username is not found.
     * @throws AuthenticationException Thrown if the supplied password does not match the stored one.
     */
    public AccessTokenResource loginUser(LoginUserResource resource) throws UserNotFoundException, AuthenticationException {
        UserEntity userEntity = userRepository.findByUsername(resource.getUsername());
        if(userEntity != null){
            if(BCrypt.checkpw(resource.getPassword(), userEntity.getPassword())){
                JWTToken token = new JWTToken(TokenUtil.generate(userEntity.getUsername(), userEntity.getPassword()));
                Subject currentUser = SecurityUtils.getSubject();
                currentUser.login(token);
                return new AccessTokenResource(userEntity.getId(), token.getCredentials().toString(), userEntity.getRole());
            } else {
                throw new AuthenticationException("Wrong password");
            }
        } else {
            throw new UserNotFoundException(resource.getUsername());
        }
    }
}
