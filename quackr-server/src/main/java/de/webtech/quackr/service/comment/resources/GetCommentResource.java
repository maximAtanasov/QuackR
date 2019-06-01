package de.webtech.quackr.service.comment.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Resource class used for retrieving comment entities.
 * Maps directly to JSON/XML.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "comment")
public class GetCommentResource {
    private Long id;

    private String text;

    private Date datePosted;

    private Long posterId;

    private Long eventId;
}
