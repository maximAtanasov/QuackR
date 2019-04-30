package de.webtech.quackr.service.comment.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.webtech.quackr.service.comment.CommentNotFoundException;
import de.webtech.quackr.service.comment.CommentService;
import de.webtech.quackr.service.comment.domain.CreateCommentResource;
import de.webtech.quackr.service.event.EventNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RestController
@Path("/comments")
public class CommentController {

    private final CommentService commentService;

    private Gson gson = new Gson();

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Handles a GET request to /events/{eventId}/comments
     * @return All comments for the selected event in the database in a JSON format. (200 OK)
     */
    @GET
    @Path("event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComments(@PathParam("eventId") long id) {
        try {
            return Response.ok(gson.toJson(commentService.getComments(id)), MediaType.APPLICATION_JSON).build();
        } catch (EventNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Handles a GET request to /events/{eventId}/comments
     * @return The created comment as JSON (200 OK), (404 NOT FOUND) If the event
     * with the given id is not found or (400 BAD REQUEST) if the JSON body is missing
     * required fields.
     */
    @POST
    @Path("event/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(CreateCommentResource resource, @PathParam("eventId") long id) {
        try {
            return Response.status(Response.Status.CREATED).entity(gson.toJson(commentService.createComment(resource, id))).type(
                    MediaType.APPLICATION_JSON).build();
        } catch (EventNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (JsonSyntaxException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Handles a GET request to /comments/{commentId}
     * @return The comment with the given id in the database in a JSON format. (200 OK)
     */
    @GET
    @Path("{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComment(@PathParam("commentId") long commentId) {
        try {
            return Response.ok(gson.toJson(commentService.getComment(commentId)), MediaType.APPLICATION_JSON).build();
        } catch (CommentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }


    /**
     * Deletes a comment from the database if it exists.
     * @param commentId The id of the event to delete.
     * @return A Response indicating successful deletion (200 OK) or an error if the user is not found
     * (404 NOT FOUND).
     */
    @DELETE
    @Path("{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComment(@PathParam("commentId") long commentId) {
        try {
            commentService.deleteComment(commentId);
            return Response.ok().build();
        } catch (CommentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Edits an comment in the database given the comment id.
     * @param resource A CreateEventResource containing the event data.
     * @param commentId The event id.
     * @return A GetCommentResource with the edited comment or a message indicating a comment with the given id does not exist (404 NOT FOUND).
     */
    @POST
    @Path("{commentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editComment(CreateCommentResource resource, @PathParam("commentId") long commentId) {
        try {
            return Response.status(Response.Status.OK).entity(gson.toJson(commentService.editComment(resource, commentId)))
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (CommentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(e.getMessage()).build();
        }
    }
}
