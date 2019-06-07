package de.webtech.quackr.service.user.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResource {
    private Long id;
    private String accessToken;
}
