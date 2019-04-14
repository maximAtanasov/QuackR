package de.webtech.quackr.service.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Resource used to provide information
 * on user creation/editing.
 */
@Getter
@AllArgsConstructor
public class CreateUserResource {
    private String username;
    private String password;
    private Long rating;
}
