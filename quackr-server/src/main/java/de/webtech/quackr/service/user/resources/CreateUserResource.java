package de.webtech.quackr.service.user.resources;

import de.webtech.quackr.persistence.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Resource class used for creating/editing users.
 * Maps directly to JSON/XML.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor // Needed for XML deserialization
public class CreateUserResource {
    @NotNull(message = "The username may not be null")
    private String username;

    @NotNull(message = "The password may not be null")
    private String password;

    @NotNull(message = "The rating may not be null")
    private Long rating;

    @NotNull(message = "The user role may not be null")
    private UserRole role;
}
