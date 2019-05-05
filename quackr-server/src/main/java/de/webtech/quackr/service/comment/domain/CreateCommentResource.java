package de.webtech.quackr.service.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Needed for XML deserialization
@AllArgsConstructor
public class CreateCommentResource {
    private String text;

    private Long posterId;
}
