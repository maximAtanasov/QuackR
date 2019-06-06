package de.webtech.quackr.service;

import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRole;
import de.webtech.quackr.service.authentication.AuthorizationService;
import de.webtech.quackr.service.authentication.ShiroRealm;
import de.webtech.quackr.service.user.UserNotFoundException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerTestTemplate {
    @Autowired
    protected TestRestTemplate restTemplate;

    @MockBean
    private ShiroRealm shiroRealm;

    @MockBean
    private AuthorizationService authorizationService;

    protected final HttpHeaders headersJSON = new HttpHeaders();
    protected final HttpHeaders headersXML = new HttpHeaders();

    protected String testPassword = BCrypt.hashpw("testPassword3", BCrypt.gensalt());


    @Before
    public void setUpSecurity() throws UserNotFoundException {
        when(authorizationService.checkTokenWithUserId(anyString(), anyLong()))
                .thenReturn(new UserEntity("testUser", testPassword, 0L, UserRole.USER));

        when(shiroRealm.supports(any())).thenReturn(true);
        when(shiroRealm.doGetAuthenticationInfo(any()))
                .thenReturn(new SimpleAuthenticationInfo("user", "user", "shiro_realm"));
    }

    @Before
    public void setUpHeaders(){
        headersJSON.add("Authorization", "asd");
        headersXML.add("Authorization", "asd");

        headersJSON.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headersXML.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
    }
}
