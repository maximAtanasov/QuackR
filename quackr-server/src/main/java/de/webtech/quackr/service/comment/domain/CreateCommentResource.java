package de.webtech.quackr.service.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCommentResource {
    private String text;

    private Long posterId;
}
