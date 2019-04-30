package de.webtech.quackr.service.event.domain;

import de.webtech.quackr.service.comment.domain.GetCommentResource;
import de.webtech.quackr.service.user.domain.GetUserResource;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

/**
 * Resource class used for retrieving event entities.
 * Maps directly to JSON.
 */
@Data
@NoArgsConstructor
public class GetEventResource {

    private Long id;

    private String title;

    private String location;

    private Date date;

    private String description;

    private Long attendeeLimit;

    private boolean isPublic;

    Collection<GetUserResource> attendees;

    Collection<GetCommentResource> comments;

}
