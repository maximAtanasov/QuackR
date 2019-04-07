package de.webtech.quackr.service.user.rest;

import com.google.gson.Gson;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.UserService;
import de.webtech.quackr.service.user.UserWithUsernameAlreadyExistsException;
import de.webtech.quackr.service.user.domain.CreateUserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Component
public class UserController {

    private final UserService userService;
    private Gson gson = new Gson();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles a GET request to /users
     * @return All users in the database in a JSON format. (200 OK)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        String json = gson.toJson(userService.getUsers());
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Handles a GET request to /users/{id}
     * @param id The id of the user to get.
     * @return The user with the given id if they exist (200 OK), or an error message (404 NOT FOUND).
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") long id) {
        try {
            return Response.ok(gson.toJson(userService.getUserById(id)), MediaType.APPLICATION_JSON).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with id " + id + " not found!").build();
        }
    }

    /**
     * Creates a new user given a CreateUserResource.
     * @param resource The resource containing the needed data.
     * @return The new user as JSON (201 CREATED) or an error message if a user with the same username already exists (400 BAD REQUEST).
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserResource resource) {
        try {
            return Response.status(Response.Status.CREATED).entity(gson.toJson(userService.createUser(resource)))
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (UserWithUsernameAlreadyExistsException e){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity("A user with the username " + resource.getUsername() + " already exists!").build();
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
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUser(CreateUserResource resource, @PathParam("id") long id) {
        try {
            return Response.status(Response.Status.OK).entity(gson.toJson(userService.editUser(resource, id)))
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (UserWithUsernameAlreadyExistsException e){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .entity("A user with the username " + resource.getUsername() + " already exists!").build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity("User with id " + id + " not found!").build();
        }
    }

    /**
     * Deletes a user from the database given it's id.
     * @param id The id of the user to delete.
     * @return A Response indicating successful deletion (200 OK) or an error if the user is not found
     * (404 NOT FOUND).
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") long id) {
        try {
            userService.deleteUser(id);
            return Response.status(Response.Status.OK).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity("User with id " + id + " not found!").build();
        }
    }
}
