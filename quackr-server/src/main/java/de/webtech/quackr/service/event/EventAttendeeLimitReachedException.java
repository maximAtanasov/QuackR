package de.webtech.quackr.service.event;

public class EventAttendeeLimitReachedException extends Throwable {
    public EventAttendeeLimitReachedException(){
        super("Cannot add more attendees to this event.");
    }
}
