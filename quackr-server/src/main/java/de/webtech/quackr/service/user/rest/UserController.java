package de.webtech.quackr.service.user.rest;

import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.ErrorResponse;
import de.webtech.quackr.service.authentication.AuthorizationService;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.UserService;
import de.webtech.quackr.service.user.UserWithUsernameAlreadyExistsException;
import de.webtech.quackr.service.user.resources.CreateUserResource;
import de.webtech.quackr.service.user.resources.EditUserResource;
import de.webtech.quackr.service.user.resources.LoginUserResource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
@RestController
public class UserController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

    @Autowired
    public UserController(UserService userService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    /**
     * Handles a GET request to /users
     * @return All users in the database in JSON or XML format. (200 OK)
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RequiresAuthentication
    public Response getUsers(@HeaderParam("accept") String accept) {
        if(accept.equals(MediaType.APPLICATION_JSON)){
            return Response.ok(userService.getUsers()).build();
        } else {
            return Response.ok(new UserCollectionXmlWrapper(userService.getUsers())).build();
        }
    }

    /**
     * Handles a GET request to /users/{id}
     * @param id The id of the user to get.
     * @return The user with the given id if they exist (200 OK), or an error message (404 NOT FOUND).
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RequiresAuthentication
    public Response getUser(@PathParam("id") long id) {
        try {
            return Response.ok(userService.getUserById(id)).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * Creates a new user given a CreateUserResource.
     * @param resource The resource containing the needed data.
     * @return The new user as JSON/XML (201 CREATED), an error message if a user with the same username already exists (409 CONFLICT) or
     * 400 BAD REQUEST if the request is missing fields or is malformed.
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createUser(@Valid @NotNull(message = "Request body may not be null") CreateUserResource resource) {
        try {
            return Response.status(Response.Status.CREATED).entity(userService.createUser(resource)).build();
        } catch (UserWithUsernameAlreadyExistsException e){
            return Response.status(Response.Status.CONFLICT.getStatusCode())
                    .entity(new ErrorResponse(e.getMessage())).build();
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
    @RequiresAuthentication
    public Response editUser(@HeaderParam("Authorization") String authorization,
                             @Valid @NotNull(message = "Request body may not be null") EditUserResource resource,
                             @PathParam("id") long id) {
        try {
            //Do not allow regular users to elevate their own privileges
            if(authorizationService.checkTokenWithUserId(authorization, id).getRole() != UserRole.ADMIN){
                resource.setRole(UserRole.USER);
            }

            return Response.ok(userService.editUser(resource, id)).build();
        } catch (UserWithUsernameAlreadyExistsException e){
            return Response.status(Response.Status.CONFLICT.getStatusCode())
                    .entity(new ErrorResponse(e.getMessage())).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(new ErrorResponse(e.getMessage())).build();
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
    @RequiresAuthentication
    public Response deleteUser(@HeaderParam("Authorization") String authorization,
                               @PathParam("id") long id) {
        try {
            authorizationService.checkTokenWithUserId(authorization, id);
            userService.deleteUser(id);
            return Response.ok().build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }


    /**
     * Logs a user in and returns a response containing the access token.
     * @param resource A LoginUserResource Object.
     * @return The access token (200 OK), 404 NOT FOUND if the user with the given username is not found
     * and 401 UNAUTHORIZED if is the password is wrong.
     */
    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response loginUser(@Valid @NotNull(message = "Request body may not be null") LoginUserResource resource) {
        try {
            return Response.ok(userService.loginUser(resource)).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(new ErrorResponse(e.getMessage())).build();
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}
