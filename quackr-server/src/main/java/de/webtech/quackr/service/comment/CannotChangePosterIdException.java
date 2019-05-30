package de.webtech.quackr.service.comment;

public class CannotChangePosterIdException extends Throwable {
    CannotChangePosterIdException(){
        super("The poster id cannot be changed");
    }
}
