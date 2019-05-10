package de.webtech.quackr.service.event.rest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import de.webtech.quackr.service.event.resources.GetEventResource;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is needed so that the list of events is nicely serialized to XML
 * (With proper tag names)
 */
@Data
@JacksonXmlRootElement(localName = "events")
class EventCollectionXmlWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "event")
    private Collection<GetEventResource> events;

    EventCollectionXmlWrapper(Collection<GetEventResource> events){
        this.events = new ArrayList<>();
        this.events.addAll(events);
    }
}
