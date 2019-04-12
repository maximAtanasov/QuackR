package de.webtech.quackr.service.event;

public class EventNotFoundException extends Throwable {
    public EventNotFoundException(long eventId) {
        super("Event with id " + eventId + " not found!");
    }
}
