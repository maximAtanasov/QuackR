package de.webtech.quackr.persistence.event;

import de.webtech.quackr.persistence.comment.CommentEntity;
import de.webtech.quackr.persistence.user.UserEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Database event entry.
 */
@Entity
@Table(name = "EVENT")
@Data
public class EventEntity {

    @Id
    @GenericGenerator(name = "EVENT_SEQUENCE", strategy = "sequence")
    @GeneratedValue(generator = "EVENT_SEQUENCE", strategy=GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer attendeeLimit;

    @Column(nullable = false)
    private boolean isPublic;

    @OneToOne
    private UserEntity organizer;

    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<UserEntity> attendees = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Collection<CommentEntity> comments = new ArrayList<>();
}
