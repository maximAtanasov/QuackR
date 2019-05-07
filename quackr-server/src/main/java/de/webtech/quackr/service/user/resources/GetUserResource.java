package de.webtech.quackr.service.user.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resource class used to handle users without
 * revealing hashed passwords.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor // Needed for XML
public class GetUserResource {
    private Long id;
    private String username;
    private Long rating;
}
