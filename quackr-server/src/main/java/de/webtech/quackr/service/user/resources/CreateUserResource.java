package de.webtech.quackr.service.user.resources;

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
    @NotNull private String username;
    @NotNull private String password;
    @NotNull private Long rating;
}
