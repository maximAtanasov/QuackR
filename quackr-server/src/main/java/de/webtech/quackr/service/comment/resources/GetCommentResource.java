package de.webtech.quackr.service.comment.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
    @NotNull private Long id;

    @NotNull private String text;

    @NotNull private Date datePosted;

    @NotNull private Long posterId;

    @NotNull private Long eventId;
}
