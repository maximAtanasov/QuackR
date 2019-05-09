package de.webtech.quackr.service.user;

import de.webtech.quackr.service.ControllerTestTemplate;
import de.webtech.quackr.service.user.resources.CreateUserResource;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;

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
    public void testCreateUser_AcceptJSON() throws UserWithUsernameAlreadyExistsException {
        Mockito.when(userService.createUser(any()))
                .thenReturn(testGetResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CreateUserResource> entity = new HttpEntity<>(testCreateResource, headers);

        ResponseEntity<GetUserResource> result = this.restTemplate.exchange("/users", HttpMethod.POST, entity, GetUserResource.class);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(testGetResource , result.getBody());
    }

    /**
     * Tests that a POST request to the /users endpoint returns proper XML.
     * @throws UserWithUsernameAlreadyExistsException Not thrown in this test
     */
    @Test
    public void testCreateUser_AcceptXML() throws UserWithUsernameAlreadyExistsException {
        Mockito.when(userService.createUser(any()))
                .thenReturn(testGetResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<CreateUserResource> entity = new HttpEntity<>(testCreateResource, headers);

        ResponseEntity<GetUserResource> result = this.restTemplate.exchange("/users", HttpMethod.POST, entity, GetUserResource.class);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(testGetResource , result.getBody());
    }

    /**
     * Tests that a GET request to the /users/{userId} endpoint returns proper JSON.
     * @throws UserNotFoundException Not thrown in this test
     */
    @Test
    public void testGetUserById_AcceptJSON() throws UserNotFoundException {
        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(testGetResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GetUserResource> result = this.restTemplate.exchange("/users/1", HttpMethod.GET, entity, GetUserResource.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testGetResource , result.getBody());
    }

    /**
     * Tests that a GET request to the /users/{userId} endpoint returns proper XML.
     * @throws UserNotFoundException Not thrown in this test
     */
    @Test
    public void testGetUserById_AcceptXML() throws UserNotFoundException {
        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(testGetResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GetUserResource> result = this.restTemplate.exchange("/users/1", HttpMethod.GET, entity, GetUserResource.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testGetResource , result.getBody());
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
     */
    @Test
    public void testGetAllUsers() {
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
