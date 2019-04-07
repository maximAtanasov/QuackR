package de.webtech.quackr.service.user.rest;

import de.webtech.quackr.service.user.UsernameAlreadyInUseException;
import de.webtech.quackr.service.user.UserNotFoundException;
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
        } catch (UsernameAlreadyInUseException e){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUser(CreateUserResource resource, @PathParam("id") long id) {
        try {
            System.out.println(id);
            userService.editUser(resource, id);
            return Response.status(Response.Status.OK.getStatusCode()).build();
        } catch (UserNotFoundException | UsernameAlreadyInUseException e){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") long id) {
        try {
            userService.deleteUser(id);
            return Response.status(Response.Status.OK).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
