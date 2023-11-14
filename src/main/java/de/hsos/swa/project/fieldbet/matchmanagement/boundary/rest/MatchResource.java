package de.hsos.swa.project.fieldbet.matchmanagement.boundary.rest;

import java.util.Collection;

import javax.annotation.security.PermitAll;
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

import de.hsos.swa.project.fieldbet.matchmanagement.control.MatchCreationDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.control.MatchFacade;
import de.hsos.swa.project.fieldbet.matchmanagement.control.MatchUpdateDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.Match;
import de.hsos.swa.project.fieldbet.shared.control.MatchDTO;

/**
 * MatchResource
 * 
 * @author Julian Voss
 */
@Path("/api/v1/matches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
public class MatchResource {

    private MatchFacade matchFacade;

    @Inject
    public MatchResource(MatchFacade matchFacade) {
        this.matchFacade = matchFacade;
    }

    @POST
    @RolesAllowed({ "admin" })
    public Response post(MatchCreationDTO creationDTO) {
        if (creationDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Match createdMatch = this.matchFacade.create(creationDTO);
        MatchDTO dto = MatchDTO.Converter.toDTO(createdMatch);
        return Response.ok(dto).build();
    }

    @GET
    @PermitAll
    public Response get() {
        Collection<Match> matches = this.matchFacade.findAll();
        if (matches.isEmpty()) {
            return Response.status(Status.NO_CONTENT).build();
        }
        Collection<MatchDTO> dtoList = MatchDTO.Converter.toDTOList(matches);
        return Response.ok(dtoList).build();
    }

    @GET
    @Path("/{matchId}")
    @PermitAll
    public Response get(@PathParam("matchId") Long matchId) {
        if (matchId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Match match = this.matchFacade.findById(matchId);
        MatchDTO dto = MatchDTO.Converter.toDTO(match);
        return Response.ok(dto).build();
    }

    @PATCH
    @Path("/{matchId}")
    @RolesAllowed({ "admin" })
    public Response patch(@PathParam("matchId") Long matchId, MatchUpdateDTO updateDTO) {
        if (matchId == null || updateDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Match updatedMatch = this.matchFacade.updateById(matchId, updateDTO);
        MatchDTO dto = MatchDTO.Converter.toDTO(updatedMatch);
        return Response.ok(dto).build();
    }

    @DELETE
    @Path("/{matchId}")
    @RolesAllowed({ "admin" })
    public Response delete(@PathParam("matchId") Long matchId) {
        if (matchId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        this.matchFacade.removeById(matchId);
        return Response.ok().build();
    }
}
