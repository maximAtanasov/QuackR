package de.webtech.quackr.persistance.event;

import de.webtech.quackr.persistance.user.UserEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "EVENT")
@Data
public class EventEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String location;

    private Date date;

    private String description;

    private Long attendeeLimit;

    @OneToMany
    private Collection<UserEntity> attendees;
}
