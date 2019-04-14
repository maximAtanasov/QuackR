package de.webtech.quackr;

import de.webtech.quackr.service.event.rest.EventController;
import de.webtech.quackr.service.user.rest.UserController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(){
        register(UserController.class);
        register(EventController.class);
    }
}
