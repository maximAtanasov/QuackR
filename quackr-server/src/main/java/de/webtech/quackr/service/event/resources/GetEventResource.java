package de.webtech.quackr.service.event.resources;

import de.webtech.quackr.service.comment.resources.GetCommentResource;
import de.webtech.quackr.service.user.resources.GetUserResource;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Resource class used for retrieving event entities.
 * Maps directly to JSON/XML.
 */
@Data
public class GetEventResource {

    @NotNull private Long id;

    @NotNull private String title;

    @NotNull private String location;

    @NotNull private Date date;

    @NotNull private String description;

    @NotNull private Long attendeeLimit;

    @NotNull private boolean isPublic;

    @NotNull Collection<GetUserResource> attendees;

    @NotNull Collection<GetCommentResource> comments;

    /**
     * Default constructor needed for the
     * deserialization from XML
     */
    public GetEventResource(){
        this.attendees = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    /**
     * Explicit setter needed so that Jackson never sets the collection to null
     * @param attendees A colleciton of GetUserResource objects.
     */
    public void setAttendees(Collection<GetUserResource> attendees) {
        if (attendees != null) {
            this.attendees = attendees;
        }
    }

    /**
     * Explicit setter needed so that Jackson never sets the collection to null
     * @param comments A collection of GetCommentResource objects.
     */
    public void setComments(Collection<GetCommentResource> comments) {
        if (comments != null) {
            this.comments = comments;
        }
    }

}
