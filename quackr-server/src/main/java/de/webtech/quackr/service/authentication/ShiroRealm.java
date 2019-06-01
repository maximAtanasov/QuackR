package de.webtech.quackr.service.authentication;

import de.webtech.quackr.persistence.user.UserEntity;
import de.webtech.quackr.persistence.user.UserRepository;
import de.webtech.quackr.service.user.UserNotFoundException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiroRealm extends AuthorizingRealm {

    private UserRepository userRepository;

    @Autowired
    public void setUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * Only when need to check user permission，for instance checkRole,checkPermission etc.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = TokenUtil.getUsername(principals.toString());
        UserEntity user = userRepository.findByUsername(username);
        if(user != null) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            simpleAuthorizationInfo.addRole(user.getRole().toString());
            //Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
            //simpleAuthorizationInfo.addStringPermissions(permission);
            return simpleAuthorizationInfo;
        } else {
            return null;
        }
    }

    /**
     * Check if username and password is corrent otherwise throw error
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        String username = TokenUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("Access token is invalid");
        }
        UserEntity user = userRepository.findByUsername(username);
        if(user == null) {
            throw new AuthenticationException(new UserNotFoundException(username).getMessage());
        }
        if (!TokenUtil.verify(token, username, user.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }
        return new SimpleAuthenticationInfo(token, token, "shiro_realm");
    }
}