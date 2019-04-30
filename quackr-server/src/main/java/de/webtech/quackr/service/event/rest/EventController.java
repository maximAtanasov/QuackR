package de.webtech.quackr.service.event.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.webtech.quackr.service.event.EventNotFoundException;
import de.webtech.quackr.service.event.EventService;
import de.webtech.quackr.service.event.domain.CreateEventResource;
import de.webtech.quackr.service.user.UserNotFoundException;
import de.webtech.quackr.service.user.domain.GetUserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@RestController
@Path("/events")
public class EventController {

    private final EventService eventService;

    private Gson gson = new Gson();

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Handles a GET request to /users/{id}/events
     * @return All events for the selected user in the database in a JSON format. (200 OK)
     */
    @GET
    @Path("user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvents(@PathParam("userId") long id) {
        try {
            return Response.ok(gson.toJson(eventService.getEvents(id)), MediaType.APPLICATION_JSON).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Handles a GET request to /users/{id}/events
     * @return The created event as JSON (200 OK), (404 NOT FOUND) If the user
     * with the given id is not found or (400 BAD REQUEST) if the JSON body is missing
     * required fields.
     */
    @POST
    @Path("user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEvent(CreateEventResource resource, @PathParam("userId") long id) {
        try {
            return Response.status(Response.Status.CREATED).entity(gson.toJson(eventService.createEvent(resource, id))).type(
                    MediaType.APPLICATION_JSON).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (JsonSyntaxException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Handles a GET request to /events/{eventId}
     * @return The event with the given id for the selected user in the database in a JSON format. (200 OK)
     */
    @GET
    @Path("{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvent(@PathParam("eventId") long eventId) {
        try {
            return Response.ok(gson.toJson(eventService.getEvent(eventId)), MediaType.APPLICATION_JSON).build();
        } catch (EventNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEvent(@PathParam("eventId") long eventId) {
        try {
            eventService.deleteEvent(eventId);
            return Response.ok().build();
        } catch (EventNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editEvent(CreateEventResource resource, @PathParam("eventId") long eventId) {
        try {
            return Response.status(Response.Status.OK).entity(gson.toJson(eventService.editEvent(resource, eventId)))
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (EventNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(e.getMessage()).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAttendees(Collection<GetUserResource> resources, @PathParam("eventId") long eventId) {
        try {
            return Response.status(Response.Status.OK).entity(gson.toJson(eventService.addEventAttendees(eventId, resources)))
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (EventNotFoundException | UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(e.getMessage()).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeAttendees(Collection<GetUserResource> resources, @PathParam("eventId") long eventId) {
        try {
            return Response.status(Response.Status.OK).entity(gson.toJson(eventService.removeEventAttendees(eventId, resources)))
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (EventNotFoundException | UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(e.getMessage()).build();
        }
    }

}
