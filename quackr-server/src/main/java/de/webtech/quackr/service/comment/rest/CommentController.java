package de.webtech.quackr.service.comment.rest;

import de.webtech.quackr.service.comment.CommentNotFoundException;
import de.webtech.quackr.service.comment.CommentService;
import de.webtech.quackr.service.comment.resources.CreateCommentResource;
import de.webtech.quackr.service.event.EventNotFoundException;
import de.webtech.quackr.service.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RestController
@Path("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Handles a GET request to /comments/event/{eventId}
     * @return All comments for the selected event in the database in a JSON or XML format. (200 OK)
     */
    @GET
    @Path("event/{eventId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCommentsForEvent(@PathParam("eventId") long id, @HeaderParam(value = "accept") String accept) {
        try {
            if(accept.equals(MediaType.APPLICATION_JSON)){
                return Response.ok(commentService.getCommentsForEvent(id)).build();
            } else {
                return Response.ok(new CommentCollectionXmlWrapper(commentService.getCommentsForEvent(id))).build();
            }
        } catch (EventNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Handles a GET request to /comments/event/{eventId}
     * @return The created comment as JSON/XML (200 OK), (404 NOT FOUND) If the event
     * with the given id is not found or (400 BAD REQUEST) if the JSON/XML body is missing
     * required fields.
     */
    @POST
    @Path("event/{eventId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addComment(@Valid CreateCommentResource resource, @PathParam("eventId") long id) {
        try {
            return Response.status(Response.Status.CREATED).entity(commentService.createComment(resource, id)).build();
        } catch (EventNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Handles a GET request to /comments/event/{eventId}
     * @return All comments for the selected event in the database in a JSON or XML format. (200 OK)
     */
    @GET
    @Path("user/{userId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCommentsForUser(@PathParam("userId") long id, @HeaderParam(value = "accept") String accept) {
        try {
            if(accept.equals(MediaType.APPLICATION_JSON)){
                return Response.ok(commentService.getCommentsForUser(id)).build();
            } else {
                return Response.ok(new CommentCollectionXmlWrapper(commentService.getCommentsForUser(id))).build();
            }
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Handles a GET request to /comments/{commentId}
     * @return The comment with the given id in the database in a JSON or XML format. (200 OK)
     */
    @GET
    @Path("{commentId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getComment(@PathParam("commentId") long commentId) {
        try {
            return Response.ok(commentService.getComment(commentId)).build();
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response editComment(@Valid CreateCommentResource resource, @PathParam("commentId") long commentId) {
        try {
            return Response.ok(commentService.editComment(resource, commentId)).build();
        } catch (CommentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .entity(e.getMessage()).build();
        }
    }
}
