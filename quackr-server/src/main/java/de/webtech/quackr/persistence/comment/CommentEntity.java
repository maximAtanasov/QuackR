package de.webtech.quackr.persistence.comment;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Database comment entry.
 */
@Entity
@Table(name = "COMMENT")
@Data
public class CommentEntity {

    @Id
    @GenericGenerator(name = "COMMENT_SEQUENCE", strategy = "sequence")
    @GeneratedValue(generator = "COMMENT_SEQUENCE", strategy= GenerationType.SEQUENCE)
    private Long id;

    private String text;

    private Date datePosted;

    private Long posterId;

    private Long eventId;
}
