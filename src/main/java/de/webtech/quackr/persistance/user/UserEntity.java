package de.webtech.quackr.persistance.user;

import de.webtech.quackr.persistance.event.EventEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "QUACKR_USER")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private Long rating;

    @OneToMany
    private Collection<EventEntity> events;

    public UserEntity(String username, String password, Long rating){
        this.username = username;
        this.password = password;
        this.rating = rating;
    }
}
