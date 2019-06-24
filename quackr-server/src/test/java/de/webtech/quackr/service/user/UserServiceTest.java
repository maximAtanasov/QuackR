package de.webtech.quackr.service.user;

import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRepository;
import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.user.resources.CreateUserResource;
import de.webtech.quackr.service.user.resources.EditUserResource;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class UserServiceTest {

    private UserRepository userRepository = mock(UserRepository.class);

    private UserService userService;

    private String testPassword = BCrypt.hashpw("testPassword3", BCrypt.gensalt());

    /**
     * Mocks all methods from the userRepository that need mocking.
     * Returns example/dummy data where required.
     */
    @Before
    public void setUp() {
        userService = new UserService(userRepository);
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(new UserEntity("testUser", testPassword, 0L, UserRole.USER)));

        Mockito.when(userRepository.findById(7L))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.existsById(7L))
                .thenReturn(false);

        Mockito.when(userRepository.findAll())
                .thenReturn(Arrays.asList(new UserEntity("testUser", testPassword, 0L, UserRole.USER),
                        new UserEntity("testUser2", testPassword, 50L, UserRole.USER)));

        Mockito.when(userRepository.existsById(1L))
                .thenReturn(true);

        Mockito.when(userRepository.existsByUsername("testUser4"))
                .thenReturn(true);

        UserEntity testEntity = new UserEntity("testUser4", testPassword, 10L, UserRole.USER);
        testEntity.setId(4L);
        Mockito.when(userRepository.findByUsername(any()))
                .thenReturn(testEntity);
    }

    /**
     * Tests the getUserById() method of the service.
     * @throws UserNotFoundException Doesn't throw in this test.
     */
    @Test
    public void testGetUserById() throws UserNotFoundException {
        GetUserResource result = userService.getUserById(1L);
        Assert.assertEquals("testUser", result.getUsername());
        Assert.assertEquals(0L, result.getRating().longValue());
    }

    /**
     * Tests that the getUserById() method of the service throws the UserNotFoundException if the user
     * is not found.
     * @throws UserNotFoundException Checked in this test.
     */
    @Test(expected = UserNotFoundException.class)
    public void testGetUserByIdThrowsExceptionWhenUserNotFound() throws UserNotFoundException {
        userService.getUserById(7L);
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
        CreateUserResource resource = new CreateUserResource("testUser3", "testPassword3", 10L, UserRole.USER);
        GetUserResource result = userService.createUser(resource);
        Mockito.verify(userRepository, Mockito.times(1)).save(any());

        Assert.assertEquals(resource.getUsername(), result.getUsername());
        Assert.assertEquals(resource.getRating(), result.getRating());
    }

    /**
     * Tests the createUser() method of the service throws an Exception when another user the same username exists.
     * @throws UserWithUsernameAlreadyExistsException Checked in this test.
     */
    @Test(expected = UserWithUsernameAlreadyExistsException.class)
    public void testCreateUserThrowsExceptionWhenUsernameExists() throws UserWithUsernameAlreadyExistsException {
        CreateUserResource resource = new CreateUserResource("testUser4", "testPassword3", 10L, UserRole.USER);
        userService.createUser(resource);
        Mockito.verify(userRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests the editUser() method of the service.
     * @throws UserWithUsernameAlreadyExistsException Not thrown in this test.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testEditUser() throws UserWithUsernameAlreadyExistsException, UserNotFoundException {
        EditUserResource resource = new EditUserResource("testUser3", "testPassword3", 10L, UserRole.USER);
        GetUserResource result = userService.editUser(resource, 1L);
        Mockito.verify(userRepository, Mockito.times(1)).save(any());


        Assert.assertEquals(resource.getUsername(), result.getUsername());
        Assert.assertEquals(resource.getRating(), result.getRating());
    }

    /**
     * Tests the editUser() method of the service.
     * @throws UserWithUsernameAlreadyExistsException Not thrown in this test.
     * @throws UserNotFoundException Checked in this test.
     */
    @Test(expected = UserNotFoundException.class)
    public void testEditUserThrowsExceptionWhenUserNotFound() throws UserWithUsernameAlreadyExistsException, UserNotFoundException {
        EditUserResource resource = new EditUserResource("testUser3", "testPassword3", 10L, UserRole.USER);
        userService.editUser(resource, 7L);
        Mockito.verify(userRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests the editUser() method of the service when a user with the same username exists.
     * @throws UserWithUsernameAlreadyExistsException Checked in this test.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test(expected = UserWithUsernameAlreadyExistsException.class)
    public void testEditUserWhenUsernameExists() throws UserWithUsernameAlreadyExistsException, UserNotFoundException {
        EditUserResource resource = new EditUserResource("testUser4", "testPassword3", 10L, UserRole.USER);
        userService.editUser(resource, 1L);
        Mockito.verify(userRepository, Mockito.times(0)).save(any());
    }

    /**
     * Tests the deleteUser() method of the service.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteUser() throws UserNotFoundException {
        userService.deleteUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).delete(any());
    }

    /**
     * Tests that the deleteUser() method of the service throws an exception when the
     * user with the given id is not found.
     * @throws UserNotFoundException Checked in this test.
     */
    @Test(expected = UserNotFoundException.class)
    public void testDeleteUserThrowsExceptionWhenUserNotFound() throws UserNotFoundException {
        userService.deleteUser(7L);
    }
}