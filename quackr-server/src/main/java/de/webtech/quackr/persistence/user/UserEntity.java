package de.webtech.quackr.persistence.user;

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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long rating;

    @Column(nullable = false)
    private UserRole role;

    public UserEntity(String username, String password, Long rating, UserRole role){
        this.username = username;
        this.password = password;
        this.rating = rating;
        this.role = role;
    }
}
