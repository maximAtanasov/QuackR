package de.webtech.quackr.service.user.rest;

import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.UserService;
import de.webtech.quackr.service.user.UserWithUsernameAlreadyExistsException;
import de.webtech.quackr.service.user.resources.CreateUserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles a GET request to /users
     * @return All users in the database in JSON or XML format. (200 OK)
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUsers() {
        return Response.ok(userService.getUsers()).build();
    }

    /**
     * Handles a GET request to /users/{id}
     * @param id The id of the user to get.
     * @return The user with the given id if they exist (200 OK), or an error message (404 NOT FOUND).
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUser(@PathParam("id") long id) {
        try {
            return Response.ok(userService.getUserById(id)).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Creates a new user given a CreateUserResource.
     * @param resource The resource containing the needed data.
     * @return The new user as JSON/XML (201 CREATED) or an error message if a user with the same username already exists (400 BAD REQUEST).
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createUser(CreateUserResource resource) {
        try {
            return Response.status(Response.Status.CREATED).entity(userService.createUser(resource)).build();
        } catch (UserWithUsernameAlreadyExistsException e){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(e.getMessage()).build();
        }
    }

    /**
     * Edits a user in the database given the user id.
     * @param resource A CreateUserResource containing the user data.
     * @param id The user id.
     * @return A GetUserResource with the edited user, an error message if another user with the same username already exists
     * (400 BAD REQUEST) or a message indicating a user with the given id does not exist (404 NOT FOUND).
     */
    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response editUser(CreateUserResource resource, @PathParam("id") long id) {
        try {
            return Response.ok(userService.editUser(resource, id)).build();
        } catch (UserWithUsernameAlreadyExistsException e){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity(e.getMessage()).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(e.getMessage()).build();
        }
    }

    /**
     * Deletes a user from the database given an id.
     * @param id The id of the user to delete.
     * @return A Response indicating successful deletion (200 OK) or an error if the user is not found
     * (404 NOT FOUND).
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteUser(@PathParam("id") long id) {
        try {
            userService.deleteUser(id);
            return Response.ok().build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(e.getMessage()).build();
        }
    }
}
