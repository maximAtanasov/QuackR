package de.webtech.quackr.service.comment.rest;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import de.webtech.quackr.service.comment.resources.GetCommentResource;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is needed so that the list of comments is nicely serialized to XML
 * (With proper tag names)
 */
@Data
@JacksonXmlRootElement(localName = "comments")
class CommentCollectionXmlWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "comment")
    private Collection<GetCommentResource> comments;

    CommentCollectionXmlWrapper(Collection<GetCommentResource> comments){
        this.comments = new ArrayList<>();
        this.comments.addAll(comments);
    }
}
