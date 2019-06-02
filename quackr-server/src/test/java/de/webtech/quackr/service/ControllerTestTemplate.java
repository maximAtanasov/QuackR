package de.webtech.quackr.service;

import de.webtech.quackr.service.authentication.ShiroRealm;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerTestTemplate {
    @Autowired
    protected TestRestTemplate restTemplate;

    @MockBean
    ShiroRealm shiroRealm;

    protected final HttpHeaders headersJSON = new HttpHeaders();
    protected final HttpHeaders headersXML = new HttpHeaders();

    @Before
    public void setUpShiro() {
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
