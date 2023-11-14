package de.hsos.swa.project.fieldbet.usermanagement.boundary.rest;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hsos.swa.project.fieldbet.usermanagement.control.UserCreationDTO;
import de.hsos.swa.project.fieldbet.usermanagement.control.UserFacade;

/**
 * UserResource
 * 
 * @author Julian Voss
 */
@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
public class UserResource {

    private UserFacade userFacade;

    @Inject
    public UserResource(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @POST
    @PermitAll
    public Response create(UserCreationDTO creationDTO) {
        String userId = this.userFacade.createUser(creationDTO);
        if (userId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        return Response.ok(userId).build();
    }
}
