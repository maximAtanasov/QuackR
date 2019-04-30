package de.webtech.quackr.persistance.comment;

import de.webtech.quackr.persistance.user.UserEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

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
