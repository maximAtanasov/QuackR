package de.webtech.quackr.service.user.resources;

import com.fasterxml.jackson.annotation.JsonRootName;
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
    @NotNull private Long id;
    @NotNull private String username;
    @NotNull private Long rating;
}
