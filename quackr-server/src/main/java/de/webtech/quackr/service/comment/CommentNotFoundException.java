package de.webtech.quackr.service.comment;

/**
 * Used to signify that a comment has not been found in the database.
 */
public class CommentNotFoundException extends Throwable{
    public CommentNotFoundException(long commentId) {
        super("Comment with id " + commentId + " not found!");
    }
}
