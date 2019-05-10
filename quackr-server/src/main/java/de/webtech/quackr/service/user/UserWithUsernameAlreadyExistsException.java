package de.webtech.quackr.service.user;

/**
 * Used to signify that a user with the given username already exists in the database.
 */
public class UserWithUsernameAlreadyExistsException extends Exception {
    UserWithUsernameAlreadyExistsException(String username) {
        super("A user with the username " + username + " already exists!");
    }
}
