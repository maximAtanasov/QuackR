package de.webtech.quackr.service.event.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Resource class used for creating/editing events.
 * Maps directly to JSON/XML.
 */
@Data
@NoArgsConstructor
public class CreateEventResource {

    @NotNull(message = "The title may not be null")
    private String title;

    @NotNull(message = "The location may not be null")
    private String location;

    @NotNull(message = "The date may not be null")
    private Date date;

    @NotNull(message = "The description may not be null")
    private String description;

    @NotNull(message = "The attendeeLimit may not be null")
    private Long attendeeLimit;

    @NotNull(message = "The attribute 'public' may not be null")
    @JsonProperty(value = "public")
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private Boolean isPublic;

    public Boolean isPublic(){
        return this.isPublic;
    }

    public void setPublic(boolean isPublic){
        this.isPublic = isPublic;
    }
}
