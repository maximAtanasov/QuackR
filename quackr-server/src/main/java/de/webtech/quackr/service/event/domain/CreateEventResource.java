package de.webtech.quackr.service.event.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Resource class used for creating/editing events.
 * Maps directly to JSON.
 */
@Data
@NoArgsConstructor
public class CreateEventResource {

    private String title;

    private String location;

    private Date date;

    private String description;

    private Long attendeeLimit;

    private boolean isPublic;
}
