package de.webtech.quackr.service;

import de.webtech.quackr.persistence.comment.CommentRepository;
import de.webtech.quackr.persistence.event.EventRepository;
import de.webtech.quackr.persistence.user.UserRepository;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Exists only as a reset mechanism for the tool ALEX.
 */
@Path("reset")
@RestController
public class ResetController {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;


    public ResetController(UserRepository userRepository, EventRepository eventRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
    }


    /**
     * Handles a GET request to /reset
     * Deletes all entities in the database.
     * @return (200 OK)
     */
    @GET
    public Response resetAll() {
        commentRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
        return Response.ok().build();
    }
}
