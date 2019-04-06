package de.webtech.quackr.service.user.rest;

import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.service.user.UserAlreadyExistsException;
import de.webtech.quackr.service.user.UserService;
import de.webtech.quackr.service.user.domain.CreateUserResource;
import de.webtech.quackr.service.user.domain.GetUserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/users")
@Component
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<GetUserResource> getUsers() {
        return userService.getUsers();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GetUserResource getUser(@PathParam("id") long id) {
        return userService.getUserById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserResource resource) {
        try {
            userService.createUser(resource);
            return Response.status(Response.Status.CREATED.getStatusCode()).build();
        } catch (UserAlreadyExistsException e){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
    }
}
