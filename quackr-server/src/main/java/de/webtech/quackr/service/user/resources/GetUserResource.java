package de.webtech.quackr.service.user.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
import de.webtech.quackr.persistence.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Resource class used for retrieving user entities.
 * Maps directly to JSON/XML.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor // Needed for XML deserialization
@JsonRootName(value = "user")
public class GetUserResource {
    @NotNull(message = "The user id may not be null")
    private Long id;

    @NotNull(message = "The username may not be null")
    private String username;

    @NotNull(message = "The rating may not be null")
    private Long rating;

    @NotNull(message = "The user role may not be null")
    private UserRole role;
}
