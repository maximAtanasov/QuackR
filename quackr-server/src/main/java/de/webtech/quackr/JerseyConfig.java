package de.webtech.quackr;

import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import de.webtech.quackr.service.ResetController;
import de.webtech.quackr.service.authentication.rest.ShiroExceptionMapper;
import de.webtech.quackr.service.comment.rest.CommentController;
import de.webtech.quackr.service.event.rest.EventController;
import de.webtech.quackr.service.user.rest.UserController;
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
        register(ResetController.class);
    }
}
