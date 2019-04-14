package de.webtech.quackr.service.user;

import de.webtech.quackr.persistance.user.UserEntity;
import de.webtech.quackr.persistance.user.UserRepository;
import de.webtech.quackr.service.user.domain.CreateUserResource;
import de.webtech.quackr.service.user.domain.GetUserResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    /**
     * Mocks all methods from the userRepository that need mocking.
     * Returns example/dummy data where required.
     */
    @Before
    public void setUp() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(new UserEntity("testUser", "testPassword", 0L)));

        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(new UserEntity("testUser", "testPassword", 0L),
                        new UserEntity("testUser2", "testPassword2", 50L)));

        Mockito.when(userRepository.existsById(any()))
                .thenReturn(true);

        Mockito.when(userRepository.findByUsername(any()))
                .thenReturn(new UserEntity("testUser", "testPassword3", 10L));

        Mockito.when(userRepository.save(any())).thenReturn(new UserEntity("testUser", "testPassword", 10L));
    }

    /**
     * Tests the getUserById() method of the service.
     * @throws UserNotFoundException Doesn't throw in this test.
     */
    @Test
    public void testGetUserById() throws UserNotFoundException {
        GetUserResource result = userService.getUserById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findById(anyLong());
        Assert.assertEquals("testUser", result.getUsername());
        Assert.assertEquals(0L, result.getRating().longValue());
    }

    /**
     * Tests the getUsers() method of the service.
     */
    @Test
    public void testGetUsers() {
        Collection<GetUserResource> result = userService.getUsers();

        Assert.assertEquals(2, result.size());
        Iterator<GetUserResource> it = result.iterator();
        Assert.assertEquals("testUser", it.next().getUsername());
        Assert.assertEquals("testUser2", it.next().getUsername());
    }

    /**
     * Tests the createUser() method of the service.
     * @throws UserWithUsernameAlreadyExistsException Doesn't throw in this test.
     */
    @Test
    public void testCreateUser() throws UserWithUsernameAlreadyExistsException {
        CreateUserResource resource = new CreateUserResource("testUser3", "testPassword3", 10L);
        GetUserResource result = userService.createUser(resource);
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
        Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(any());

        Assert.assertEquals(resource.getUsername(), result.getUsername());
        Assert.assertEquals(resource.getRating(), result.getRating());
    }

    /**
     * Tests the editUser() method of the service.
     * @throws UserWithUsernameAlreadyExistsException Not thrown in this test.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testEditUser() throws UserWithUsernameAlreadyExistsException, UserNotFoundException {
        CreateUserResource resource = new CreateUserResource("testUser3", "testPassword3", 10L);
        GetUserResource result = userService.editUser(resource, 1L);
        Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(any());
        Mockito.verify(userRepository, Mockito.times(1)).save(any());


        Assert.assertEquals(resource.getUsername(), result.getUsername());
        Assert.assertEquals(resource.getRating(), result.getRating());
    }

    /**
     * Tests the deleteUser() method of the service.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteUser() throws UserNotFoundException {
        userService.deleteUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).existsById(any());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}