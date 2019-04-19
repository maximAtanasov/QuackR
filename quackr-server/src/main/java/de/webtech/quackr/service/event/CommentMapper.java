package de.webtech.quackr.service.event;

import de.webtech.quackr.AbstractMapper;
import de.webtech.quackr.persistance.event.CommentEntity;
import de.webtech.quackr.service.event.domain.GetCommentResource;
import de.webtech.quackr.service.user.UserMapper;

public class CommentMapper extends AbstractMapper<GetCommentResource, CommentEntity> {

    private final UserMapper userMapper = new UserMapper();

    @Override
    public GetCommentResource map(CommentEntity entity){
        return new GetCommentResource(entity.getId(),
                entity.getText(),entity.getDatePosted(), entity.getPosterId());
    }
}
