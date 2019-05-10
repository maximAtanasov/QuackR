package de.webtech.quackr.service.user.rest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import de.webtech.quackr.service.user.resources.GetUserResource;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is needed so that the list of users is nicely serialized to XML
 * (With proper tag names)
 */
@Data
@JacksonXmlRootElement(localName = "users")
class UserCollectionXmlWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "user")
    private Collection<GetUserResource> users;

    UserCollectionXmlWrapper(Collection<GetUserResource> users){
        this.users = new ArrayList<>();
        this.users.addAll(users);
    }
}
