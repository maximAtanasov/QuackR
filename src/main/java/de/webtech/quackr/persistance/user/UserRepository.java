package de.webtech.quackr.persistance.user;

import de.webtech.quackr.service.user.domain.UserResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserResource findUserEntityByUsername(String username);
}
