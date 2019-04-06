package de.webtech.quackr.service.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserResource {
    private String username;
    private String password;
}
