package de.hsos.swa.project.fieldbet.betmanagement.boundary.web;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;

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

import org.eclipse.microprofile.jwt.JsonWebToken;

import de.hsos.swa.project.fieldbet.betmanagement.control.BetCreationDTO;
import de.hsos.swa.project.fieldbet.betmanagement.control.BetFacade;
import de.hsos.swa.project.fieldbet.betmanagement.control.BetUpdateDTO;
import de.hsos.swa.project.fieldbet.betmanagement.entity.Bet;
import de.hsos.swa.project.fieldbet.matchmanagement.control.MatchFacade;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.Match;
import de.hsos.swa.project.fieldbet.shared.control.BetDTO;
import de.hsos.swa.project.fieldbet.shared.control.MatchDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;

/**
 * BetWebResource
 * 
 * @author Patrick Felschen
 */
@Path("/web/v1/bets")
@Produces(MediaType.TEXT_HTML)
@Transactional(TxType.REQUIRES_NEW)
@RequestScoped
public class BetWebResource {
    private BetFacade betFacade;

    private MatchFacade matchFacade;

    private JsonWebToken jwt;

    @Inject
    public BetWebResource(BetFacade betFacade, MatchFacade matchFacade, JsonWebToken jwt) {
        this.betFacade = betFacade;
        this.matchFacade = matchFacade;
        this.jwt = jwt;
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance BetListTemplate(
                boolean isAuthenticated,
                Collection<BetDTO> bets,
                Collection<MatchDTO> matches);
    }

    @POST
    @Authenticated
    public Response post(
            @FormParam("matchId") Long matchId,
            @FormParam("goalsTeam1") Integer goalsTeam1,
            @FormParam("goalsTeam2") Integer goalsTeam2) {

        BetCreationDTO creationDTO = new BetCreationDTO(matchId, goalsTeam1, goalsTeam2);

        String userId = jwt.getSubject();
        this.betFacade.create(userId, creationDTO);

        return Response.seeOther(URI.create("/web/v1/bets")).build();
    }

    @GET
    public TemplateInstance get() {
        String userId = jwt.getSubject();
        Collection<Match> matches = this.matchFacade.findNotFinished();
        Collection<Bet> bets = this.betFacade.findNotFinishedByProfileId(userId);

        Collection<MatchDTO> allMatchDTOs = MatchDTO.Converter.toDTOList(matches);
        Collection<BetDTO> allBetDTOs = BetDTO.Converter.toDTOList(bets);

        Iterator<MatchDTO> allMatchDTOIterator = allMatchDTOs.iterator();
        while (allMatchDTOIterator.hasNext()) {
            MatchDTO matchDTO = allMatchDTOIterator.next();
            for (BetDTO betDTO : allBetDTOs) {
                if (matchDTO.id.equals(betDTO.match.id)) {
                    allMatchDTOIterator.remove();
                }
            }
        }

        return Templates.BetListTemplate(userId != null, allBetDTOs, allMatchDTOs);
    }

    @POST
    @Path("/{betId}")
    @Authenticated
    public Response patch(
            @PathParam("betId") Long betId,
            @FormParam("goalsTeam1") Integer goalsTeam1,
            @FormParam("goalsTeam2") Integer goalsTeam2) {

        BetUpdateDTO updateDTO = new BetUpdateDTO(goalsTeam1, goalsTeam2);

        String userId = jwt.getSubject();

        this.betFacade.updateById(userId, betId, updateDTO);

        return Response.seeOther(URI.create("/web/v1/bets")).build();
    }

}
