package de.webtech.quackr.rest;

import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import de.webtech.quackr.rest.authentication.ShiroExceptionMapper;
import de.webtech.quackr.rest.comment.CommentController;
import de.webtech.quackr.rest.event.EventController;
import de.webtech.quackr.rest.user.UserController;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(){
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        register(ShiroExceptionMapper.class);
        register(JacksonXMLProvider.class);
        register(UserController.class);
        register(EventController.class);
        register(CommentController.class);
    }
}
