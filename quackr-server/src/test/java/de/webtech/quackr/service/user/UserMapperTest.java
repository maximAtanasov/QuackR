package de.webtech.quackr.service.user;

import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the UserMapper class.
 */
public class UserMapperTest {

    private UserMapper userMapper;

    @Before
    public void setUp(){
        userMapper = new UserMapper();
    }

    /**
     * Tests the mapping of a single UserEntity to a GetUserResource object.
     */
    @Test
    public void testMapEntityToResource(){
        UserEntity entity = new UserEntity("testUser", "testPassword", 10L, UserRole.ADMIN);
        entity.setId(1L);

        GetUserResource result = userMapper.map(entity);
        Assert.assertEquals(1L, result.getId().longValue());
        Assert.assertEquals("testUser", result.getUsername());
        Assert.assertEquals(10L, result.getRating().longValue());
        Assert.assertEquals(UserRole.ADMIN, result.getRole());
    }

    /**
     * Tests the mapping of a collection of UserEntities to a list of GetUserResources.
     */
    @Test
    public void testMapEntitiesToResources(){
        UserEntity entity1 = new UserEntity("testUser1", "testPassword", 10L, UserRole.USER);
        entity1.setId(1L);

        UserEntity entity2 = new UserEntity("testUser2", "testPassword", 20L, UserRole.USER);
        entity2.setId(2L);

        UserEntity entity3 = new UserEntity("testUser3", "testPassword", 30L, UserRole.USER);
        entity3.setId(3L);

        List<GetUserResource> result = new ArrayList<>(userMapper.map(Arrays.asList(entity1, entity2, entity3)));
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
