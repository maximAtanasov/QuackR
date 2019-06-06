package de.webtech.quackr.service.user;

/**
 * Used to signify that a user has not been found in the database.
 */
public class UserNotFoundException extends Throwable {
    public UserNotFoundException(long userId) {
        super("User with id " + userId + " not found!");
    }
    public UserNotFoundException(String username) {
        super("User with username " + username + " not found!");
    }
}
