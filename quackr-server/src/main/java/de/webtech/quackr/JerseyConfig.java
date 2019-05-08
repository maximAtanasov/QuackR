package de.webtech.quackr;

import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import de.webtech.quackr.service.comment.rest.CommentController;
import de.webtech.quackr.service.event.rest.EventController;
import de.webtech.quackr.service.user.rest.UserController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(){
        register(JacksonXMLProvider.class);
        register(UserController.class);
        register(EventController.class);
        register(CommentController.class);
    }
}
