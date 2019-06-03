package de.webtech.quackr.service.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;


public class TokenUtil {

    private static final long TOKEN_EXPIRATION_DURATION = 60*24*7*60*1000;

    /**
     * Check if the token is valid
     * @param token The token to check
     * @param secret The password to check against.
     * @return True if the token is valid, false otherwise.
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }


    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * Generates a JWT that expires in 15 minutes.
     * @param username The username
     * @param password The password
     * @return TOKEN
     */
    public static String generate(String username, String password) {
        try {
            Date expirationDate = new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_DURATION);
            Algorithm algorithm = Algorithm.HMAC256(password);
            return JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
