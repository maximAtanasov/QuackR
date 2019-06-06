package de.webtech.quackr.service.comment;

import de.webtech.quackr.persistence.comment.CommentEntity;
import de.webtech.quackr.persistence.comment.CommentRepository;
import de.webtech.quackr.persistence.event.EventEntity;
import de.webtech.quackr.persistence.event.EventRepository;
import de.webtech.quackr.persistence.user.UserRepository;
import de.webtech.quackr.service.comment.resources.CreateCommentResource;
import de.webtech.quackr.service.comment.resources.GetCommentResource;
import de.webtech.quackr.service.event.EventNotFoundException;
import de.webtech.quackr.service.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommentService {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CommentMapper commentMapper = new CommentMapper();

    @Autowired
    CommentService(CommentRepository commentRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    /**
     * Return all comments for the selected event.
     * @param eventId The id of the event.
     * @return All comments for the selected event.
     * @throws EventNotFoundException Thrown when the selected event is not found.
     */
    public Collection<GetCommentResource> getCommentsForEvent(long eventId) throws EventNotFoundException {
        if(eventRepository.existsById(eventId)){
            return commentMapper.map(commentRepository.findByEventId(eventId));
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    /**
     * Return all comments for the selected user.
     * @param userId the id of the user
     * @return All comments for the selected user.
     * @throws UserNotFoundException Thrown when the selected user is not found.
     */
    public Collection<GetCommentResource> getCommentsForUser(long userId) throws UserNotFoundException {
        if(userRepository.existsById(userId)){
            return commentMapper.map(commentRepository.findByPosterId(userId));
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * Creates a comment.
     * @param resource A CreateCommentResource object.
     * @param eventId The id of the event we're commenting.
     * @return A GetCommentResource object.
     * @throws EventNotFoundException Thrown if the event with eventId is not found.
     * @throws UserNotFoundException Thrown if the user with the if specifed in the resource is not found.
     */
    public GetCommentResource createComment(CreateCommentResource resource, long eventId) throws EventNotFoundException, UserNotFoundException {
        Optional<EventEntity> event = eventRepository.findById(eventId);
        if(event.isPresent()){
            if(!userRepository.existsById(resource.getPosterId())){
                throw new UserNotFoundException(resource.getPosterId());
            }
            CommentEntity newComment = new CommentEntity();
            newComment.setEventId(eventId);
            newComment.setDatePosted(new Date());
            newComment.setPosterId(resource.getPosterId());
            newComment.setText(resource.getText());
            event.get().getComments().add(newComment);
            commentRepository.save(newComment);
            eventRepository.save(event.get());
            return commentMapper.map(newComment);
        } else {
            throw new EventNotFoundException(eventId);
        }
    }


    /**
     * Retrieves a single comment from the database.
     * @param commentId The id of the comment.
     * @return A GetCommentResource object.
     * @throws CommentNotFoundException Thrown if the comment is not found.
     */
    public GetCommentResource getComment(long commentId) throws CommentNotFoundException {
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
        if(commentEntity.isPresent()){
            return commentMapper.map(commentEntity.get());
        }else{
            throw new CommentNotFoundException(commentId);
        }
    }

    /**
     * Delete a comment from the database.
     * @param commentId The id of the comment to delete.
     * @throws CommentNotFoundException Thrown if the comment is not found.
     */
    public void deleteComment(long commentId) throws CommentNotFoundException {
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
        if(commentEntity.isPresent()){
            commentRepository.delete(commentEntity.get());
        }else{
            throw new CommentNotFoundException(commentId);
        }
    }

    /**
     * Edits an existing comment in the database.
     * @param resource A CreateCommentResource containing the new data.
     * @param commentId The id of the comment to change.
     * @return A GetCommentResource object.
     * @throws CommentNotFoundException Thrown if the event is not found.
     */
    public GetCommentResource editComment(CreateCommentResource resource, long commentId) throws CommentNotFoundException,
            CannotChangePosterIdException {
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
        if(commentEntity.isPresent()){
            commentEntity.get().setText(resource.getText());
            if(!resource.getPosterId().equals(commentEntity.get().getPosterId())){
                throw new CannotChangePosterIdException();
            }
            commentRepository.save(commentEntity.get());
            return commentMapper.map(commentEntity.get());
        }else{
            throw new CommentNotFoundException(commentId);
        }
    }
}
