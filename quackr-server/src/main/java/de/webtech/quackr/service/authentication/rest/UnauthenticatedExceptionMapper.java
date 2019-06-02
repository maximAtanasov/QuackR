package de.webtech.quackr.service.authentication.rest;

import de.webtech.quackr.service.ErrorResponse;
import org.apache.shiro.authz.UnauthenticatedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Catches the UnauthenticatedException thrown by Shiro and returns a 401 JSON response.
 */
@Provider
public class UnauthenticatedExceptionMapper implements ExceptionMapper<UnauthenticatedException> {

    @Override
    public Response toResponse(UnauthenticatedException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("Authentication failed")).build();
    }
}