package de.webtech.quackr.service.comment.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Resource class used for creating/editing comments.
 * Maps directly to JSON/XML.
 */
@Data
@NoArgsConstructor // Needed for XML deserialization
@AllArgsConstructor
public class CreateCommentResource {

    @NotNull(message = "The text must not be null")
    private String text;

    @NotNull(message = "The posterId must not be null")
    private Long posterId;
}
