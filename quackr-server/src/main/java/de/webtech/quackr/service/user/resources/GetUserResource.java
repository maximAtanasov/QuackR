package de.webtech.quackr.service.user.resources;

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
public class GetUserResource {
    @NotNull private Long id;
    @NotNull private String username;
    @NotNull private Long rating;
}
