package de.webtech.quackr.service.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResource {

    private Long id;
    private String username;
}
