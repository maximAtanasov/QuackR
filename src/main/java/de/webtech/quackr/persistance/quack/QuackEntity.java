package de.webtech.quackr.persistance.quack;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "QUACK")
@Data
public class QuackEntity {

    @Id
    private Long id;

    private String title;

    private String content;
}
