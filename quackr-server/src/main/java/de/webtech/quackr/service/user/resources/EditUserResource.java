package de.webtech.quackr.service.user.resources;

import de.webtech.quackr.persistence.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Resource class used for editing users.
 * Maps directly to JSON/XML.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor // Needed for XML deserialization
public class EditUserResource {
    @NotNull(message = "The username may not be null")
    @Size(max = 100)
    private String username;

    @Size(max = 100)
    private String password; //Password can be null,

    @NotNull(message = "The rating may not be null")
    private Long rating;

    @NotNull(message = "The user role may not be null")
    private UserRole role;
}
