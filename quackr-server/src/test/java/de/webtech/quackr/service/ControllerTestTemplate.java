package de.webtech.quackr.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerTestTemplate {
    @Autowired
    protected TestRestTemplate restTemplate;

    protected final HttpHeaders headersJSON = new HttpHeaders();
    protected final HttpHeaders headersXML = new HttpHeaders();

    @Before
    public void setUpHeaders(){
        headersJSON.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headersXML.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
    }
}
