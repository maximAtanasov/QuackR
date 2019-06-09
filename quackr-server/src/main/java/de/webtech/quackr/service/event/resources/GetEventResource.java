package de.webtech.quackr.service.event.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
@JsonRootName(value = "event")
@JacksonXmlRootElement(localName = "event")
public class GetEventResource {

    private Long id;

    private Long organizerId;

    private String title;

    private String location;

    private Date date;

    private String description;

    private Integer attendeeLimit;

    private boolean isPublic;

    @JacksonXmlElementWrapper(localName = "attendees")
    @JacksonXmlProperty(localName = "user")
    @NotNull Collection<GetUserResource> attendees;

    @JacksonXmlElementWrapper(localName = "comments")
    @JacksonXmlProperty(localName = "comment")
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
     * @param attendees A collection of GetUserResource objects.
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
