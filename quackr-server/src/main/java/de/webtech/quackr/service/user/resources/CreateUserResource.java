package de.webtech.quackr.service.user.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Resource used to provide information
 * on user creation/editing.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor // Needed for XML
public class CreateUserResource {
    private String username;
    private String password;
    private Long rating;
}
