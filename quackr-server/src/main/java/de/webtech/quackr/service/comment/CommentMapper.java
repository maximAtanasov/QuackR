package de.webtech.quackr.service.comment;

import de.webtech.quackr.persistence.comment.CommentEntity;
import de.webtech.quackr.service.AbstractMapper;
import de.webtech.quackr.service.comment.resources.GetCommentResource;

public class CommentMapper extends AbstractMapper<GetCommentResource, CommentEntity> {

    @Override
    public GetCommentResource map(CommentEntity entity){
        return new GetCommentResource(entity.getId(),
                entity.getText(),entity.getDatePosted(), entity.getPosterId(), entity.getEventId());
    }
}
