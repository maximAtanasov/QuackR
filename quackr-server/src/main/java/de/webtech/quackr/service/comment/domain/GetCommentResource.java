package de.webtech.quackr.service.comment.domain;

import de.webtech.quackr.service.user.domain.GetUserResource;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GetCommentResource {
    private Long id;

    private String text;

    private Date datePosted;

    private Long posterId;

    private Long eventId;
}
