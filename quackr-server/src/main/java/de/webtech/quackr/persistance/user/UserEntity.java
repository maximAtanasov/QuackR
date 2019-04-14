package de.webtech.quackr.persistance.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Database user entry.
 */
@Entity
@Table(name = "QUACKR_USER")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GenericGenerator(name = "QUACK_USER_SEQUENCE", strategy = "sequence")
    @GeneratedValue(generator = "QUACK_USER_SEQUENCE", strategy=GenerationType.SEQUENCE)
    private Long id;

    private String username;

    private String password;

    private Long rating;

    public UserEntity(String username, String password, Long rating){
        this.username = username;
        this.password = password;
        this.rating = rating;
    }
}