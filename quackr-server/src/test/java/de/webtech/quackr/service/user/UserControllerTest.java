package de.webtech.quackr.service.user;

import de.webtech.quackr.service.ControllerTestTemplate;
import de.webtech.quackr.service.user.resources.CreateUserResource;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class UserControllerTest extends ControllerTestTemplate {

    @MockBean
    private UserService userService;

    private CreateUserResource testCreateResource;
    private GetUserResource testGetResource;

    /**
     * Tests set up
     */
    @Before
    public void setUp() {
        testGetResource = new GetUserResource(1L, "testUser", 50L);
        testCreateResource = new CreateUserResource("testUser", "testPassword", 50L);
    }

    /**
     * Tests that a POST request to the /users endpoint returns proper JSON.
     * @throws UserWithUsernameAlreadyExistsException Not thrown in this test
     */
    @Test
    public void testCreateUser() throws UserWithUsernameAlreadyExistsException {
        Mockito.when(userService.createUser(any()))
                .thenReturn(testGetResource);

        ResponseEntity<GetUserResource> entity = this.restTemplate.postForEntity("/users", testCreateResource, GetUserResource.class);
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(testGetResource , entity.getBody());
    }

    /**
     * Tests that a GET request to the /users/{userId} endpoint returns proper JSON.
     * @throws UserNotFoundException Not thrown in this test
     */
    @Test
    public void testGetUserById() throws UserNotFoundException {
        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(testGetResource);

        ResponseEntity<GetUserResource> entity = this.restTemplate.getForEntity("/users/1", GetUserResource.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(testGetResource , entity.getBody());
    }

    /**
     * Tests that a POST request to the /users/{userId} endpoint returns proper JSON.
     * @throws UserNotFoundException Not thrown in this test
     * @throws UserWithUsernameAlreadyExistsException Not thrown in this test
     */
    @Test
    public void testUpdateUser() throws UserNotFoundException, UserWithUsernameAlreadyExistsException {
        Mockito.when(userService.editUser(any(), anyLong()))
                .thenReturn(testGetResource);

        ResponseEntity<GetUserResource> entity = this.restTemplate.postForEntity("/users/1", testCreateResource, GetUserResource.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(testGetResource , entity.getBody());
    }

    /**
     * Tests that a GET request to the /users endpoint returns proper JSON.
     * @throws UserNotFoundException Not thrown in this test
     */
    @Test
    public void testGetAllUsers() throws UserNotFoundException {
        Mockito.when(userService.getUsers())
                .thenReturn(Collections.singletonList(testGetResource));

        ResponseEntity<GetUserResource[]> entity = this.restTemplate.getForEntity("/users", GetUserResource[].class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        GetUserResource[] expected = {testGetResource};
        assertArrayEquals(expected , entity.getBody());
    }

    /**
     * Tests that a DELETE request to the /users/{userId} endpoint calls the correct service method.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteUser() throws UserNotFoundException {
        this.restTemplate.delete("/users/1");
        Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
    }
}
