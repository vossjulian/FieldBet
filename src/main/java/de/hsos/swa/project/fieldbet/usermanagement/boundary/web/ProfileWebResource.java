package de.hsos.swa.project.fieldbet.usermanagement.boundary.web;

import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.swa.project.fieldbet.shared.control.ProfileDTO;
import de.hsos.swa.project.fieldbet.usermanagement.control.ProfileFacade;
import de.hsos.swa.project.fieldbet.usermanagement.entity.Profile;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;

/**
 * ProfileWebResource
 * 
 * @author Patrick Felschen
 */
@Path("/web/v1/profiles")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
@Authenticated
public class ProfileWebResource {
    private ProfileFacade profileFacade;
    private JsonWebToken jwt;

    @Inject
    public ProfileWebResource(ProfileFacade profileFacade, JsonWebToken jwt) {
        this.profileFacade = profileFacade;
        this.jwt = jwt;
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance ProfileListTemplate(
                Collection<ProfileDTO> profiles);

        public static native TemplateInstance OwnProfileTemplate(boolean isAdmin, ProfileDTO profile);
    }

    @GET
    public TemplateInstance get() {
        Collection<Profile> profiles = this.profileFacade.findAll();
        Collection<ProfileDTO> dtoList = ProfileDTO.Converter.toDTOList(profiles);
        return Templates.ProfileListTemplate(dtoList);
    }

    @GET
    @Path("/me")
    public TemplateInstance getMe() {
        String userId = jwt.getSubject();
        boolean isAdmin = jwt.getGroups().contains("admin");
        Profile profile = this.profileFacade.findById(userId);
        ProfileDTO dto = ProfileDTO.Converter.toDTO(profile);
        return Templates.OwnProfileTemplate(isAdmin, dto);
    }
}
