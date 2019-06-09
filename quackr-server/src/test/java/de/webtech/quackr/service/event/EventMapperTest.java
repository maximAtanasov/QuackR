package de.webtech.quackr.service.event;

import de.webtech.quackr.persistence.event.EventEntity;
import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.service.event.resources.GetEventResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Tests for the EventMapper class.
 */
public class EventMapperTest {

    private EventMapper eventMapper;

    @Before
    public void setUp(){
        eventMapper = new EventMapper();
    }

    /**
     * Tests the mapping of a single EventEntity to a GetEventResource object.
     */
    @Test
    public void testMapEntityToResource(){
        EventEntity entity = new EventEntity();
        entity.setTitle("BBQ");
        entity.setDescription("BBQ at Stan");
        entity.setAttendeeLimit(20);
        entity.setOrganizer(new UserEntity());
        entity.setLocation("Stan's place");
        entity.setDate(new Date());
        entity.setPublic(true);
        entity.setAttendees(new ArrayList<>());
        entity.setComments(new ArrayList<>());
        entity.setId(1L);

        GetEventResource result = eventMapper.map(entity);
        Assert.assertEquals(entity.getId().longValue(), result.getId().longValue());
        Assert.assertEquals(entity.getTitle(), result.getTitle());
        Assert.assertEquals(entity.getDescription(), result.getDescription());
        Assert.assertEquals(entity.getAttendeeLimit().longValue(), result.getAttendeeLimit().longValue());
        Assert.assertEquals(entity.getLocation(), result.getLocation());
        Assert.assertEquals(entity.getDate(), result.getDate());
        Assert.assertEquals(entity.getOrganizer().getId(), result.getOrganizerId());
        Assert.assertNotNull(entity.getAttendees());
        Assert.assertNotNull(entity.getComments());
    }

    /**
     * Tests the mapping of a single EventEntity to a GetEventResource object.
     */
    @Test
    public void testMapEntityToResourceNullLists(){
        EventEntity entity = new EventEntity();
        entity.setTitle("BBQ");
        entity.setDescription("BBQ at Stan");
        entity.setAttendeeLimit(20);
        entity.setOrganizer(new UserEntity());
        entity.setLocation("Stan's place");
        entity.setDate(new Date());
        entity.setPublic(true);
        entity.setAttendees(null);
        entity.setComments(null);
        entity.setId(1L);

        GetEventResource result = eventMapper.map(entity);
        Assert.assertEquals(entity.getId().longValue(), result.getId().longValue());
        Assert.assertEquals(entity.getTitle(), result.getTitle());
        Assert.assertEquals(entity.getDescription(), result.getDescription());
        Assert.assertEquals(entity.getAttendeeLimit().longValue(), result.getAttendeeLimit().longValue());
        Assert.assertEquals(entity.getLocation(), result.getLocation());
        Assert.assertEquals(entity.getDate(), result.getDate());
        Assert.assertNotNull(result.getAttendees());
        Assert.assertNotNull(result.getComments());
    }

    /**
     * Tests the mapping of a collection of EventEntity (ies) to a list of GetEventResources.
     */
    @Test
    public void testMapEntitiesToResources(){
        EventEntity entity1 = new EventEntity();
        entity1.setTitle("BBQ1");
        entity1.setDescription("BBQ at Stan1");
        entity1.setAttendeeLimit(20);
        entity1.setOrganizer(new UserEntity());
        entity1.setLocation("Stan's place1");
        entity1.setDate(new Date());
        entity1.setPublic(true);
        entity1.setAttendees(new ArrayList<>());
        entity1.setComments(new ArrayList<>());
        entity1.setId(1L);

        EventEntity entity2 = new EventEntity();
        entity2.setTitle("BBQ2");
        entity2.setDescription("BBQ at Stan2");
        entity2.setAttendeeLimit(22);
        entity2.setOrganizer(new UserEntity());
        entity2.setLocation("Stan's place2");
        entity2.setDate(new Date());
        entity2.setPublic(false);
        entity2.setAttendees(new ArrayList<>());
        entity2.setComments(null);
        entity2.setId(2L);

        EventEntity entity3 = new EventEntity();
        entity3.setTitle("BBQ3");
        entity3.setDescription("BBQ at Stan3");
        entity3.setAttendeeLimit(23);
        entity3.setOrganizer(new UserEntity());
        entity3.setLocation("Stan's place3");
        entity3.setDate(new Date());
        entity3.setPublic(true);
        entity3.setComments(null);
        entity3.setAttendees(new ArrayList<>());
        entity3.setId(3L);

        List<EventEntity> entities = Arrays.asList(entity1, entity2, entity3);
        List<GetEventResource> result = new ArrayList<>(eventMapper.map(entities));
        Assert.assertEquals(3, result.size());

        for(int i = 0; i < result.size(); i++){
            Assert.assertEquals(entities.get(i).getId().longValue(), result.get(i).getId().longValue());
            Assert.assertEquals(entities.get(i).getTitle(), result.get(i).getTitle());
            Assert.assertEquals(entities.get(i).getDescription(), result.get(i).getDescription());
            Assert.assertEquals(entities.get(i).getAttendeeLimit().longValue(), result.get(i).getAttendeeLimit().longValue());
            Assert.assertEquals(entities.get(i).getLocation(), result.get(i).getLocation());
            Assert.assertEquals(entities.get(i).getDate(), result.get(i).getDate());
            Assert.assertEquals(entities.get(i).getOrganizer().getId(), result.get(i).getOrganizerId());
            Assert.assertNotNull(result.get(i).getComments());
            Assert.assertNotNull(result.get(i).getAttendees());
        }
    }
}
