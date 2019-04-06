package de.webtech.quackr.service.user.rest;

import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.persistance.user.UserRepository;
import de.webtech.quackr.service.user.UserAlreadyExistsException;
import de.webtech.quackr.service.user.UserService;
import de.webtech.quackr.service.user.domain.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
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
    public Collection<UserResource> getUsers() {
        return userService.getUsers();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserResource getUser(@PathParam("id") long id) {
        return userService.getUserById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(UserEntity resource) {
        try {
            userService.createUser(resource.getUsername(), resource.getPassword());
            return Response.status(Response.Status.CREATED.getStatusCode()).build();
        } catch (UserAlreadyExistsException e){
            return Response.status(Response.Status.CONFLICT.getStatusCode()).build();
        }
    }

}
