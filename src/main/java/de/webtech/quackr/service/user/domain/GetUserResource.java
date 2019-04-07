package de.webtech.quackr.service.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserResource {
    private Long id;
    private String username;
    private Long rating;
}
