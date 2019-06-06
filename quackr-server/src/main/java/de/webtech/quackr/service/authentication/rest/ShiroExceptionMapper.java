package de.webtech.quackr.service.authentication.rest;

import de.webtech.quackr.service.ErrorResponse;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Catches any exception thrown by Shiro and returns the appropriate JSON responses.
 */
@Provider
public class ShiroExceptionMapper implements ExceptionMapper<ShiroException> {

    @Override
    public Response toResponse(ShiroException e) {
        if( e instanceof UnauthenticatedException){
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("Authentication failed")).build();
        } else if (e instanceof AuthorizationException){
            return Response.status(Response.Status.FORBIDDEN).entity(new ErrorResponse(e.getMessage())).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}