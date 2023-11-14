package de.hsos.swa.project.fieldbet.matchmanagement.boundary.web;

import java.net.URI;
import java.time.LocalDateTime;
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

import de.hsos.swa.project.fieldbet.matchmanagement.control.MatchCreationDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.control.MatchFacade;
import de.hsos.swa.project.fieldbet.matchmanagement.control.MatchUpdateDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.control.TeamFacade;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.Match;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.Team;
import de.hsos.swa.project.fieldbet.shared.control.MatchDTO;
import de.hsos.swa.project.fieldbet.shared.control.TeamDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

/**
 * MatchWebResource
 * 
 * @author Patrick Felschen
 */
@Path("/web/v1/matches")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
@RolesAllowed({ "admin" })
public class MatchWebResource {
    private MatchFacade matchFacade;

    private TeamFacade teamFacade;

    @Inject
    public MatchWebResource(MatchFacade matchFacade, TeamFacade teamFacade) {
        this.matchFacade = matchFacade;
        this.teamFacade = teamFacade;
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance MatchListTemplate(
                Collection<MatchDTO> matches,
                Collection<TeamDTO> teams);
    }

    @POST
    public Response post(
            @FormParam("team1Id") Long team1Id,
            @FormParam("team2Id") Long team2Id,
            @FormParam("startDateTime") String startDateTime) {

        MatchCreationDTO creationDTO = new MatchCreationDTO(
                team1Id,
                team2Id,
                LocalDateTime.parse(startDateTime));

        this.matchFacade.create(creationDTO);

        return Response.seeOther(URI.create("/web/v1/matches")).build();
    }

    @GET
    public TemplateInstance get() {
        Collection<Match> matches = this.matchFacade.findAll();
        Collection<Team> teams = this.teamFacade.findAll();
        Collection<MatchDTO> matchDtoList = MatchDTO.Converter.toDTOList(matches);
        Collection<TeamDTO> teamDtoList = TeamDTO.Converter.toDTOList(teams);
        return Templates.MatchListTemplate(matchDtoList, teamDtoList);
    }

    @POST
    @Path("/{matchId}")
    public Response patch(
            @PathParam("matchId") Long matchId,
            @FormParam("goalsTeam1") Integer goalsTeam1,
            @FormParam("goalsTeam2") Integer goalsTeam2,
            @FormParam("isFinished") String isFinished,
            @FormParam("startDateTime") String startDateTime) {

        MatchUpdateDTO updateDTO = new MatchUpdateDTO(
                goalsTeam1,
                goalsTeam2,
                LocalDateTime.parse(startDateTime),
                isFinished != null);

        this.matchFacade.updateById(matchId, updateDTO);

        return Response.seeOther(URI.create("/web/v1/matches")).build();
    }

    @GET
    @Path("/{matchId}/delete")
    public Response delete(@PathParam("matchId") Long matchId) {
        this.matchFacade.removeById(matchId);

        return Response.seeOther(URI.create("/web/v1/matches")).build();
    }
}
