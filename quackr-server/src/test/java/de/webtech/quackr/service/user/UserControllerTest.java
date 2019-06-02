package de.webtech.quackr.service.user;

import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.ControllerTestTemplate;
import de.webtech.quackr.service.user.resources.CreateUserResource;
import de.webtech.quackr.service.user.resources.GetUserResource;
import org.apache.shiro.realm.Realm;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class UserControllerTest extends ControllerTestTemplate {

    @MockBean
    private UserService userService;

    @MockBean
    private Realm realm;

    private CreateUserResource testCreateResource;
    private GetUserResource testGetResource;

    /**
     * Tests set up
     */
    @Before
    public void setUp() {
        testGetResource = new GetUserResource(1L, "testUser", 50L, UserRole.USER);
        testCreateResource = new CreateUserResource("testUser", "testPassword", 50L, UserRole.USER);
    }

    /**
     * Tests that a POST request to the /users endpoint returns proper JSON/XML.
     * @throws UserWithUsernameAlreadyExistsException Not thrown in this test
     */
    @Test
    public void testCreateUser() throws UserWithUsernameAlreadyExistsException {
        when(userService.createUser(any()))
                .thenReturn(testGetResource);

        // Test JSON
        HttpEntity<CreateUserResource> entity1 = new HttpEntity<>(testCreateResource, headersJSON);

        ResponseEntity<GetUserResource> result1 = this.restTemplate.exchange("/users", HttpMethod.POST, entity1, GetUserResource.class);
        assertEquals(HttpStatus.CREATED, result1.getStatusCode());
        assertEquals(testGetResource , result1.getBody());

        // Test XML
        HttpEntity<CreateUserResource> entity2 = new HttpEntity<>(testCreateResource, headersXML);

        ResponseEntity<GetUserResource> result2 = this.restTemplate.exchange("/users", HttpMethod.POST, entity2, GetUserResource.class);
        assertEquals(HttpStatus.CREATED, result2.getStatusCode());
        assertEquals(testGetResource , result2.getBody());
    }

    /**
     * Tests that a GET request to the /users/{userId} endpoint returns proper JSON/XML.
     * @throws UserNotFoundException Not thrown in this test
     */
    @Test
    public void testGetUserById() throws UserNotFoundException {
        when(userService.getUserById(anyLong()))
                .thenReturn(testGetResource);

        // Test JSON
        HttpEntity<String> entity1 = new HttpEntity<>(headersJSON);

        ResponseEntity<GetUserResource> result1 = this.restTemplate.exchange("/users/1", HttpMethod.GET, entity1, GetUserResource.class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(testGetResource , result1.getBody());

        // Test XML
        HttpEntity<String> entity2 = new HttpEntity<>(headersXML);

        ResponseEntity<GetUserResource> result2 = this.restTemplate.exchange("/users/1", HttpMethod.GET, entity2, GetUserResource.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(testGetResource , result2.getBody());
    }

    /**
     * Tests that a POST request to the /users/{userId} endpoint returns proper JSON/XML.
     * @throws UserNotFoundException Not thrown in this test
     * @throws UserWithUsernameAlreadyExistsException Not thrown in this test
     */
    @Test
    public void testUpdateUser() throws UserNotFoundException, UserWithUsernameAlreadyExistsException {
        when(userService.editUser(any(), anyLong()))
                .thenReturn(testGetResource);

        // Test JSON
        HttpEntity<CreateUserResource> entity1 = new HttpEntity<>(testCreateResource, headersJSON);

        ResponseEntity<GetUserResource> result1 = this.restTemplate.postForEntity("/users/1", entity1, GetUserResource.class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertEquals(testGetResource, result1.getBody());

        // Test XML
        HttpEntity<CreateUserResource> entity2 = new HttpEntity<>(testCreateResource, headersXML);

        ResponseEntity<GetUserResource> result2 = this.restTemplate.postForEntity("/users/1", entity2, GetUserResource.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(testGetResource, result2.getBody());
    }

    /**
     * Tests that a GET request to the /users endpoint returns proper JSON/XML.
     */
    @Test
    public void testGetAllUsers() {
        when(userService.getUsers())
                .thenReturn(Collections.singletonList(testGetResource));

        GetUserResource[] expected = {testGetResource};

        // Test JSON
        HttpEntity<String> entity1 = new HttpEntity<>(headersJSON);

        ResponseEntity<GetUserResource[]> result1 = this.restTemplate.exchange("/users", HttpMethod.GET, entity1, GetUserResource[].class);
        assertEquals(HttpStatus.OK, result1.getStatusCode());
        assertArrayEquals(expected, result1.getBody());

        // Test XML
        HttpEntity<String> entity2 = new HttpEntity<>(headersXML);

        ResponseEntity<GetUserResource[]> result2 = this.restTemplate.exchange("/users", HttpMethod.GET, entity2, GetUserResource[].class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertArrayEquals(expected, result2.getBody());
    }

    /**
     * Tests that a DELETE request to the /users/{userId} endpoint calls the correct service method.
     * @throws UserNotFoundException Not thrown in this test.
     */
    @Test
    public void testDeleteUser() throws UserNotFoundException {
        HttpEntity entity = new HttpEntity<>(headersXML);
        ResponseEntity result2 = this.restTemplate.exchange("/users/1", HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
    }
}
