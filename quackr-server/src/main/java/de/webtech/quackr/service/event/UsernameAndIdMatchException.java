package de.webtech.quackr.service.event;

public class UsernameAndIdMatchException extends Throwable {
    public UsernameAndIdMatchException(Long id, String username) {
        super("Username " + username + " does not belong to user with id " + id);
    }
}
