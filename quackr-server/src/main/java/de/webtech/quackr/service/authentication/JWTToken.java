package de.webtech.quackr.service.authentication;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * A class representing a JSON Web Token
 */
public class JWTToken implements AuthenticationToken {

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
