package de.webtech.quackr.service.user.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Resource class used to supply login information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserResource {

    @NotNull(message = "The username may not be null")
    @Size(max = 100)
    private String username;

    @NotNull(message = "The password may not be null")
    @Size(max = 100)
    private String password;
}
