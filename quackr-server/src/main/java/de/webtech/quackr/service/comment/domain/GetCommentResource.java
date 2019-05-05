package de.webtech.quackr.service.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentResource {
    private Long id;

    private String text;

    private Date datePosted;

    private Long posterId;

    private Long eventId;
}
