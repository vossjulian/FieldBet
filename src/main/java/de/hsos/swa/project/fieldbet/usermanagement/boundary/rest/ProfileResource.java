package de.hsos.swa.project.fieldbet.usermanagement.boundary.rest;

import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.swa.project.fieldbet.shared.control.ProfileDTO;
import de.hsos.swa.project.fieldbet.usermanagement.control.ProfileCreationDTO;
import de.hsos.swa.project.fieldbet.usermanagement.control.ProfileFacade;
import de.hsos.swa.project.fieldbet.usermanagement.entity.Profile;
import io.quarkus.security.Authenticated;

/**
 * ProfileResource
 * 
 * @author Patrick Felschen
 */
@Path("/api/v1/profiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
@Authenticated
public class ProfileResource {
    private ProfileFacade profileFacade;
    private JsonWebToken jwt;

    @Inject
    public ProfileResource(ProfileFacade profileFacade, JsonWebToken jwt) {
        this.profileFacade = profileFacade;
        this.jwt = jwt;
    }

    @POST
    public Response post(ProfileCreationDTO creationDTO) {
        String userId = jwt.getSubject();
        Profile createdProfile = this.profileFacade.create(userId, creationDTO);
        ProfileDTO dto = ProfileDTO.Converter.toDTO(createdProfile);
        return Response.ok(dto).build();
    }

    @GET
    public Response get() {
        Collection<Profile> profiles = this.profileFacade.findAll();
        if (profiles.isEmpty()) {
            return Response.status(Status.NO_CONTENT).build();
        }
        Collection<ProfileDTO> dtoList = ProfileDTO.Converter.toDTOList(profiles);
        return Response.ok(dtoList).build();
    }

    @GET
    @Path("/{profileId}")
    public Response get(@PathParam("profileId") String profileId) {
        if (profileId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Profile profile = this.profileFacade.findById(profileId);
        ProfileDTO dto = ProfileDTO.Converter.toDTO(profile);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/me")
    public Response getMe() {
        String userId = jwt.getSubject();
        Profile profile = this.profileFacade.findById(userId);
        ProfileDTO dto = ProfileDTO.Converter.toDTO(profile);
        return Response.ok(dto).build();
    }
}
