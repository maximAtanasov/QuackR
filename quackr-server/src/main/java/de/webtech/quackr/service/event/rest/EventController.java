package de.webtech.quackr.service.event.rest;

import de.webtech.quackr.service.ErrorResponse;
import de.webtech.quackr.service.authentication.AuthorizationService;
import de.webtech.quackr.service.event.EventAttendeeLimitReachedException;
import de.webtech.quackr.service.event.EventNotFoundException;
import de.webtech.quackr.service.event.EventService;
import de.webtech.quackr.service.event.UsernameAndIdMatchException;
import de.webtech.quackr.service.event.resources.CreateEventResource;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@RestController
@Path("events")
public class EventController {

    private final EventService eventService;
    private final AuthorizationService authorizationService;

    @Autowired
    public EventController(EventService eventService, AuthorizationService authorizationService) {
        this.eventService = eventService;
        this.authorizationService = authorizationService;
    }

    /**
     * Handles a GET request to /users/{id}/events
     * @return All events for the selected user in the database in JSON or XML format. (200 OK)
     */
    @GET
    @Path("user/{userId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RequiresAuthentication
    public Response getEvents(@PathParam("userId") long id, @HeaderParam("accept") String accept) {
        try {
            if(accept.equals(MediaType.APPLICATION_JSON)){
                return Response.ok(eventService.getEvents(id)).build();
            } else {
                return Response.ok(new EventCollectionXmlWrapper(eventService.getEvents(id))).build();
            }
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * Handles a GET request to /users/{id}/events
     * @return The created event as JSON/XML (200 OK), (404 NOT FOUND) If the user
     * with the given id is not found or (400 BAD REQUEST) if the JSON/XML body is missing
     * required fields.
     */
    @POST
    @Path("user/{userId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RequiresAuthentication
    public Response addEvent(@HeaderParam("Authorization") String authorization,
                             @Valid @NotNull(message = "Request body may not be null") CreateEventResource resource,
                             @PathParam("userId") long id) {
        try {
            authorizationService.checkTokenWithUserId(authorization, id);
            return Response.status(Response.Status.CREATED).entity(eventService.createEvent(resource, id)).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * Handles a GET request to /events/{eventId}
     * @return The event with the given id for the selected user in the database in JSON or XML format. (200 OK)
     */
    @GET
    @Path("{eventId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RequiresAuthentication
    public Response getEvent(@PathParam("eventId") long eventId) {
        try {
            return Response.ok(eventService.getEvent(eventId)).build();
        } catch (EventNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * Deletes an event from the database if it exists.
     * @param eventId The id of the event to delete.
     * @return A Response indicating successful deletion (200 OK) or an error if the user is not found
     * (404 NOT FOUND).
     */
    @DELETE
    @Path("{eventId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RequiresAuthentication
    public Response deleteEvent(@HeaderParam("Authorization") String authorization,
                                @PathParam("eventId") long eventId) {
        try {
            authorizationService.checkTokenWithEventId(authorization, eventId);
            eventService.deleteEvent(eventId);
            return Response.ok().build();
        } catch (EventNotFoundException | UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * Edits an event in the database given the event id.
     * @param resource A CreateEventResource containing the event data.
     * @param eventId The event id.
     * @return A GetEventResource with the edited user, an error message if another user with the same username already exists
     * (400 BAD REQUEST) or a message indicating a user with the given id does not exist (404 NOT FOUND).
     */
    @POST
    @Path("{eventId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RequiresAuthentication
    public Response editEvent(@HeaderParam("Authorization") String authorization,
                              @Valid @NotNull(message = "Request body may not be null") CreateEventResource resource,
                              @PathParam("eventId") long eventId) {
        try {
            authorizationService.checkTokenWithEventId(authorization, eventId);
            return Response.ok(eventService.editEvent(resource, eventId)).build();
        } catch (EventNotFoundException | UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * Adds attendees to an event in the database given the event id and the attendees.
     * @param resources A Collection of GetUserResources.
     * @param eventId The event id.
     * @return A GetEventResource with the edited user, an error message if another user with the same username already exists
     * (400 BAD REQUEST) or a message indicating a user with the given id does not exist (404 NOT FOUND).
     */
    @POST
    @Path("{eventId}/add")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RequiresAuthentication
    public Response addAttendees(@HeaderParam("Authorization") String authorization,
                                 @Valid @NotNull(message = "Request body may not be null") Collection<GetUserResource> resources,
                                 @PathParam("eventId") long eventId) {
        try {
            authorizationService.checkToken(authorization);
            return Response.ok(eventService.addEventAttendees(eventId, resources)).build();
        } catch (EventNotFoundException | UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(new ErrorResponse(e.getMessage())).build();
        } catch (UsernameAndIdMatchException | EventAttendeeLimitReachedException e) {
            return Response.status(422)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    /**
     * Removes attendees to an event in the database given the event id and the attendees.
     * @param resources A Collection of GetUserResources.
     * @param eventId The event id.
     * @return A GetEventResource with the edited event or a message indicating an event/user with the given id does not exist (404 NOT FOUND).
     */
    @POST
    @Path("{eventId}/remove")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RequiresAuthentication
    public Response removeAttendees(@HeaderParam("Authorization") String authorization,
                                    @Valid @NotNull(message = "Request body may not be null") Collection<GetUserResource> resources,
                                    @PathParam("eventId") long eventId) {
        try {
            authorizationService.checkToken(authorization);
            return Response.ok(eventService.removeEventAttendees(eventId, resources)).build();
        } catch (EventNotFoundException | UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(new ErrorResponse(e.getMessage())).build();
        } catch (UsernameAndIdMatchException e) {
            return Response.status(422)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}
