package de.webtech.quackr.service.user.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Resource class used for creating/editing users.
 * Maps directly to JSON/XML.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor // Needed for XML deserialization
public class CreateUserResource {
    private String username;
    private String password;
    private Long rating;
}
