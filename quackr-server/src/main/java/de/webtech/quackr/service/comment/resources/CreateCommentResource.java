package de.webtech.quackr.service.comment.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resource class used for creating/editing comments.
 * Maps directly to JSON/XML.
 */
@Data
@NoArgsConstructor // Needed for XML deserialization
@AllArgsConstructor
public class CreateCommentResource {
    private String text;

    private Long posterId;
}
