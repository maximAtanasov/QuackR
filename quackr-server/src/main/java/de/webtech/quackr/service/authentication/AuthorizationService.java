package de.webtech.quackr.service.authentication;

import de.webtech.quackr.persistence.comment.CommentEntity;
import de.webtech.quackr.persistence.comment.CommentRepository;
import de.webtech.quackr.persistence.event.EventEntity;
import de.webtech.quackr.persistence.event.EventRepository;
import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRepository;
import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.comment.CommentNotFoundException;
import de.webtech.quackr.service.event.EventNotFoundException;
import de.webtech.quackr.service.user.UserNotFoundException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public AuthorizationService(UserRepository userRepository, EventRepository eventRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
    }

    public UserEntity checkTokenWithUserId(String token, Long id) throws UserNotFoundException {
            String username = TokenUtil.getUsername(token);
            UserEntity userToCheck = userRepository.findByUsername(username);
            if(userToCheck != null) {
            if(userToCheck.getRole().equals(UserRole.ADMIN)){
                return userToCheck;
            } else if(!userToCheck.getId().equals(id)){
                throw new AuthorizationException("You are unauthorized to perform this action");
            } else {
                return userToCheck;
            }
        } else {
            throw new UserNotFoundException(username);
        }
    }

    public UserEntity checkToken(String token) throws UserNotFoundException, EventNotFoundException {
        Long userId;
        String username = TokenUtil.getUsername(token);
        UserEntity userToCheck = userRepository.findByUsername(username);
        if(userToCheck != null) {
            userId = userToCheck.getId();
            return checkTokenWithUserId(token, userId);
        } else {
          throw new UserNotFoundException(username);
        }
    }

    public UserEntity checkTokenWithCommentId(String token, Long commentId) throws UserNotFoundException, CommentNotFoundException {
        Long userId;
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
        if(commentEntity.isPresent()){
            userId = commentEntity.get().getPosterId();
        } else {
            throw new CommentNotFoundException(commentId);
        }
        return checkTokenWithUserId(token, userId);
    }

    public UserEntity checkTokenWithEventId(String token, Long eventId) throws UserNotFoundException, EventNotFoundException {
        Long userId;
        Optional<EventEntity> eventEntity = eventRepository.findById(eventId);
        if(eventEntity.isPresent()){
            userId = eventEntity.get().getOrganizer().getId();
        } else {
            throw new EventNotFoundException(eventId);
        }
        return checkTokenWithUserId(token, userId);
    }

}
