package de.hsos.swa.project.fieldbet.usermanagement.boundary.web;

import java.net.URI;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hsos.swa.project.fieldbet.usermanagement.control.UserCreationDTO;
import de.hsos.swa.project.fieldbet.usermanagement.control.UserFacade;

/**
 * UserWebResource
 * 
 * @author Patrick Felschen
 */
@Path("/web/v1/users")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
public class UserWebResource {
    private UserFacade userFacade;

    @Inject
    public UserWebResource(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @POST
    @PermitAll
    @Path("/signup")
    public Response create(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("firstname") String firstname,
            @FormParam("lastname") String lastname) {
        UserCreationDTO creationDTO = new UserCreationDTO(username, password, firstname, lastname);
        String userId = this.userFacade.createUser(creationDTO);
        if (userId == null) {
            return Response.seeOther(URI.create("/web/v1/bets")).build();
        }
        return Response.seeOther(URI.create("/web/v1/profiles/me")).build();
    }

}
