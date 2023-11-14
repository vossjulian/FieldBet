package de.hsos.swa.project.fieldbet.matchmanagement.boundary.rest;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.hsos.swa.project.fieldbet.matchmanagement.control.TeamCreationDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.control.TeamFacade;
import de.hsos.swa.project.fieldbet.matchmanagement.control.TeamUpdateDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.Team;
import de.hsos.swa.project.fieldbet.shared.control.TeamDTO;

/**
 * TeamResource
 * 
 * @author Julian Voss
 */
@Path("/api/v1/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
@RolesAllowed({ "admin" })
public class TeamResource {

    private TeamFacade teamFacade;

    @Inject
    public TeamResource(TeamFacade teamFacade) {
        this.teamFacade = teamFacade;
    }

    @POST
    public Response post(TeamCreationDTO creationDTO) {
        if (creationDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Team createdTeam = this.teamFacade.create(creationDTO);
        TeamDTO dto = TeamDTO.Converter.toDTO(createdTeam);
        return Response.ok(dto).build();
    }

    @GET
    public Response get() {
        Collection<Team> teams = this.teamFacade.findAll();
        if (teams.isEmpty()) {
            return Response.status(Status.NO_CONTENT).build();
        }
        Collection<TeamDTO> dtoList = TeamDTO.Converter.toDTOList(teams);
        return Response.ok(dtoList).build();
    }

    @GET
    @Path("/{teamId}")
    public Response get(@PathParam("teamId") Long teamId) {
        if (teamId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Team team = this.teamFacade.findById(teamId);
        TeamDTO dto = TeamDTO.Converter.toDTO(team);
        return Response.ok(dto).build();
    }

    @PATCH
    @Path("/{teamId}")
    public Response patch(@PathParam("teamId") Long teamId, TeamUpdateDTO updateDTO) {
        if (teamId == null || updateDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Team updatedTeam = this.teamFacade.updateById(teamId, updateDTO);
        TeamDTO dto = TeamDTO.Converter.toDTO(updatedTeam);
        return Response.ok(dto).build();
    }

    @DELETE
    @Path("/{teamId}")
    public Response delete(@PathParam("teamId") Long teamId) {
        if (teamId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        this.teamFacade.removeById(teamId);
        return Response.ok().build();
    }

}
