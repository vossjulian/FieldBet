package de.hsos.swa.project.fieldbet.matchmanagement.boundary.web;

import java.net.URI;
import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hsos.swa.project.fieldbet.matchmanagement.control.TeamCreationDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.control.TeamFacade;
import de.hsos.swa.project.fieldbet.matchmanagement.control.TeamUpdateDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.Team;
import de.hsos.swa.project.fieldbet.shared.control.TeamDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

/**
 * TeamWebResource
 * 
 * @author Patrick Felschen
 */
@Path("/web/v1/teams")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
@RolesAllowed({ "admin" })
public class TeamWebResource {

    private TeamFacade teamFacade;

    @Inject
    public TeamWebResource(TeamFacade teamFacade) {
        this.teamFacade = teamFacade;
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance TeamListTemplate(Collection<TeamDTO> teams);
    }

    @POST
    public Response post(
            @FormParam("name") String name,
            @FormParam("alias") String alias) {
        TeamCreationDTO creationDTO = new TeamCreationDTO(name, alias);

        this.teamFacade.create(creationDTO);

        return Response.seeOther(URI.create("/web/v1/teams")).build();
    }

    @GET
    public TemplateInstance get() {
        Collection<Team> teams = this.teamFacade.findAll();
        Collection<TeamDTO> dtoList = TeamDTO.Converter.toDTOList(teams);
        return Templates.TeamListTemplate(dtoList);
    }

    @POST
    @Path("/{teamId}")
    public Response patch(
            @PathParam("teamId") Long teamId,
            @FormParam("name") String name,
            @FormParam("alias") String alias) {
        TeamUpdateDTO updateDTO = new TeamUpdateDTO(name, alias);

        this.teamFacade.updateById(teamId, updateDTO);

        return Response.seeOther(URI.create("/web/v1/teams")).build();
    }

    @GET
    @Path("/{teamId}/delete")
    public Response delete(@PathParam("teamId") Long teamId) {
        this.teamFacade.removeById(teamId);

        return Response.seeOther(URI.create("/web/v1/teams")).build();
    }

}
