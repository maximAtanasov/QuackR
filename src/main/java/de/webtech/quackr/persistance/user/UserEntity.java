package de.webtech.quackr.persistance.user;

import de.webtech.quackr.persistance.quack.QuackEntity;
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

    @OneToMany
    private Collection<QuackEntity> quacks;

    public UserEntity(String username, String password){
        this.username = username;
        this.password = password;
    }
}
