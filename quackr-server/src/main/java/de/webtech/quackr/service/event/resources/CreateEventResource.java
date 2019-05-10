package de.webtech.quackr.service.event.resources;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Resource class used for creating/editing events.
 * Maps directly to JSON/XML.
 */
@Data
@NoArgsConstructor
public class CreateEventResource {

    @NotNull private String title;

    @NotNull private String location;

    @NotNull private Date date;

    @NotNull private String description;

    @NotNull private Long attendeeLimit;

    @NotNull private boolean isPublic;
}
