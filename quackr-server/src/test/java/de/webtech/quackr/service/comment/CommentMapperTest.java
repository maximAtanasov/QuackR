package de.webtech.quackr.service.comment;

import de.webtech.quackr.persistence.comment.CommentEntity;
import de.webtech.quackr.service.comment.resources.GetCommentResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommentMapperTest {

    private CommentMapper commentMapper;

    @Before
    public void setUp(){
        commentMapper = new CommentMapper();
    }

    /**
     * Tests the mapping of a single CommentEntity to a GetCommentResource object.
     */
    @Test
    public void testMapEntityToResource(){
        CommentEntity entity = new CommentEntity();
        entity.setId(1L);
        entity.setDatePosted(new Date());
        entity.setPosterId(2L);
        entity.setText("Comment text");

        GetCommentResource result = commentMapper.map(entity);
        Assert.assertEquals(entity.getId().longValue(), result.getId().longValue());
        Assert.assertEquals(entity.getDatePosted(), result.getDatePosted());
        Assert.assertEquals(entity.getText(), result.getText());
        Assert.assertEquals(entity.getPosterId(), result.getPosterId());
        Assert.assertEquals(entity.getEventId(), result.getEventId());
    }

    /**
     * Tests the mapping of a collection of CommentEntity (ies) to a list of GetCommentResources.
     */
    @Test
    public void testMapEntitiesToResources(){
        CommentEntity entity1 = new CommentEntity();
        entity1.setId(1L);
        entity1.setDatePosted(new Date());
        entity1.setPosterId(9L);
        entity1.setText("Comment text1");

        CommentEntity entity2 = new CommentEntity();
        entity2.setId(2L);
        entity2.setDatePosted(new Date());
        entity2.setPosterId(2L);
        entity2.setText("Comment text2");

        CommentEntity entity3 = new CommentEntity();
        entity3.setId(3L);
        entity3.setDatePosted(new Date());
        entity3.setPosterId(5L);
        entity3.setText("Comment text3");

        List<CommentEntity> entities = Arrays.asList(entity1, entity2, entity3);
        List<GetCommentResource> result = new ArrayList<>(commentMapper.map(entities));
        Assert.assertEquals(3, result.size());

        for(int i = 0; i < result.size(); i++){
            Assert.assertEquals(entities.get(i).getId().longValue(), result.get(i).getId().longValue());
            Assert.assertEquals(entities.get(i).getDatePosted(), result.get(i).getDatePosted());
            Assert.assertEquals(entities.get(i).getText(), result.get(i).getText());
            Assert.assertEquals(entities.get(i).getPosterId(), result.get(i).getPosterId());
            Assert.assertEquals(entities.get(i).getEventId(), result.get(i).getEventId());
        }
    }
}
