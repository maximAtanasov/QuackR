package de.webtech.quackr.service.event.domain;

import de.webtech.quackr.service.user.domain.GetUserResource;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
public class CreateEventResource {

    private String title;

    private String location;

    private Date date;

    private String description;

    private Long attendeeLimit;
}
