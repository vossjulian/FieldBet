package de.hsos.swa.project.fieldbet.betmanagement.boundary.rest;

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

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.swa.project.fieldbet.betmanagement.control.BetCreationDTO;
import de.hsos.swa.project.fieldbet.betmanagement.control.BetFacade;
import de.hsos.swa.project.fieldbet.betmanagement.control.BetUpdateDTO;
import de.hsos.swa.project.fieldbet.betmanagement.entity.Bet;
import de.hsos.swa.project.fieldbet.shared.control.BetDTO;
import io.quarkus.security.Authenticated;

/**
 * BetResource
 * 
 * @author Patrick Felschen, Julian Voss
 */
@Path("/api/v1/bets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
@Authenticated
public class BetResource {

    private BetFacade betFacade;
    private JsonWebToken jwt;

    @Inject
    public BetResource(BetFacade betFacade, JsonWebToken jwt) {
        this.betFacade = betFacade;
        this.jwt = jwt;
    }

    @POST
    public Response post(BetCreationDTO creationDTO) {
        if (creationDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        String userId = jwt.getSubject();
        Bet createdBet = this.betFacade.create(userId, creationDTO);
        BetDTO dto = BetDTO.Converter.toDTO(createdBet);
        return Response.ok(dto).build();
    }

    @GET
    @RolesAllowed({ "admin" })
    public Response get() {
        Collection<Bet> bets = this.betFacade.findAll();
        if (bets.isEmpty()) {
            return Response.status(Status.NO_CONTENT).build();
        }
        Collection<BetDTO> dtoList = BetDTO.Converter.toDTOList(bets);
        return Response.ok(dtoList).build();
    }

    @GET
    @Path("/{betId}")
    public Response get(@PathParam("betId") Long betId) {
        if (betId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        String userId = jwt.getSubject();
        Bet bet = this.betFacade.findById(userId, betId);
        BetDTO dto = BetDTO.Converter.toDTO(bet);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/me")
    public Response getOwn() {
        String userId = jwt.getSubject();
        Collection<Bet> bets = this.betFacade.findByProfileId(userId);
        if (bets.isEmpty()) {
            return Response.status(Status.NO_CONTENT).build();
        }
        Collection<BetDTO> dtoList = BetDTO.Converter.toDTOList(bets);
        return Response.ok(dtoList).build();
    }

    @PATCH
    @Path("/{betId}")
    public Response patch(@PathParam("betId") Long betId, BetUpdateDTO updateDTO) {
        if (betId == null || updateDTO == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        String userId = jwt.getSubject();
        Bet updatedbet = this.betFacade.updateById(userId, betId, updateDTO);
        BetDTO dto = BetDTO.Converter.toDTO(updatedbet);
        return Response.ok(dto).build();
    }

    @DELETE
    @Path("/{betId}")
    public Response delete(@PathParam("betId") Long betId) {
        if (betId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        String userId = jwt.getSubject();
        this.betFacade.removeById(userId, betId);
        return Response.ok().build();
    }
}
