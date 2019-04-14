package de.webtech.quackr.service.user;

import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.service.user.domain.GetUserResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the UserMapper class.
 */
public class UserMapperTest {

    /**
     * Tests the mapping of a single UserEntity to a GetUserResource object.
     */
    @Test
    public void testMapEntityToResource(){
        UserEntity entity = new UserEntity("testUser", "testPassword", 10L);
        entity.setId(1L);

        GetUserResource result = UserMapper.map(entity);
        Assert.assertEquals(1L, result.getId().longValue());
        Assert.assertEquals("testUser", result.getUsername());
        Assert.assertEquals(10L, result.getRating().longValue());
    }

    /**
     * Tests the mapping of a collection of UserEntities to a list of GetUserResources.
     */
    @Test
    public void testMapEntitiesToResources(){
        UserEntity entity1 = new UserEntity("testUser1", "testPassword", 10L);
        entity1.setId(1L);

        UserEntity entity2 = new UserEntity("testUser2", "testPassword", 20L);
        entity2.setId(2L);

        UserEntity entity3 = new UserEntity("testUser3", "testPassword", 30L);
        entity3.setId(3L);

        List<GetUserResource> result = UserMapper.map(Arrays.asList(entity1, entity2, entity3));
        Assert.assertEquals(3, result.size());

        Assert.assertEquals(1L, result.get(0).getId().longValue());
        Assert.assertEquals("testUser1", result.get(0).getUsername());
        Assert.assertEquals(10L, result.get(0).getRating().longValue());

        Assert.assertEquals(2L, result.get(1).getId().longValue());
        Assert.assertEquals("testUser2", result.get(1).getUsername());
        Assert.assertEquals(20L, result.get(1).getRating().longValue());

        Assert.assertEquals(3L, result.get(2).getId().longValue());
        Assert.assertEquals("testUser3", result.get(2).getUsername());
        Assert.assertEquals(30L, result.get(2).getRating().longValue());
    }
}
