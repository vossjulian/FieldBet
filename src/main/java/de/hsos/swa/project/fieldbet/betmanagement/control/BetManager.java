package de.hsos.swa.project.fieldbet.betmanagement.control;

import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import de.hsos.swa.project.fieldbet.betmanagement.entity.Bet;
import de.hsos.swa.project.fieldbet.betmanagement.entity.BetCatalog;
import de.hsos.swa.project.fieldbet.shared.control.IncreaseProfilePointsDTO;
import de.hsos.swa.project.fieldbet.shared.control.MatchFinishedDTO;
import de.hsos.swa.project.fieldbet.shared.control.ProfilePointsDTO;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityCreationException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityDeleteException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityQueryException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityReadException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityUpdateException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.OperationNotAllowedException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.ReferenceNotFoundException;
import io.quarkus.security.identity.SecurityIdentity;

/**
 * BetManager
 * 
 * @author Patrick Felschen, Julian Voss
 */
@ApplicationScoped
@Retry(maxRetries = 4, abortOn = {
        EntityCreationException.class,
        EntityDeleteException.class,
        EntityQueryException.class,
        EntityReadException.class,
        EntityUpdateException.class,
        ReferenceNotFoundException.class,
        OperationNotAllowedException.class,
})
@Timeout(250)
@CircuitBreaker(requestVolumeThreshold = 4, skipOn = {
        EntityCreationException.class,
        EntityDeleteException.class,
        EntityQueryException.class,
        EntityReadException.class,
        EntityUpdateException.class,
        ReferenceNotFoundException.class,
        OperationNotAllowedException.class,
})
public class BetManager implements BetFacade {

    private BetCatalog betCatalog;
    private SecurityIdentity securityIdentity;

    @Inject
    Event<IncreaseProfilePointsDTO> increaseProfilePointsEvent;

    @Inject
    public BetManager(BetCatalog betCatalog, SecurityIdentity securityIdentity) {
        this.betCatalog = betCatalog;
        this.securityIdentity = securityIdentity;
    }

    @Override
    @Counted(name = "createdBets", description = "How many bets have been created.")
    @Timed(name = "createdBetsTimer", description = "A measure of how long it takes create a bet", unit = MetricUnits.MILLISECONDS)
    public Bet create(String profileId, BetCreationDTO creationDTO) {
        return betCatalog.create(creationDTO.matchId, profileId, creationDTO.goalsTeam1, creationDTO.goalsTeam2);
    }

    @Override
    @Timed(name = "getBetTimer", description = "A measure of how long it takes to get one bet", unit = MetricUnits.MILLISECONDS)
    public Bet findById(String profileId, Long betId) {
        return betCatalog.findById(betId);
    }

    @Override
    @Timed(name = "getBetsTimer", description = "A measure of how long it takes to get all bets by userId", unit = MetricUnits.MILLISECONDS)
    public Collection<Bet> findAll() {
        return betCatalog.findAll();
    }

    @Override
    @Timed(name = "findNotFinishedBetsTimer", description = "A measure of how long it takes to get not finished bets by userId", unit = MetricUnits.MILLISECONDS)
    public Collection<Bet> findNotFinishedByProfileId(String profileId) {
        return betCatalog.findNotFinishedByProfileId(profileId);
    }

    @Override
    @Timed(name = "updateBetTimer", description = "A measure of how long it takes to update one bet", unit = MetricUnits.MILLISECONDS)
    public Bet updateById(String profileId, Long betId, BetUpdateDTO updateDTO) {
        Bet bet = this.betCatalog.findById(betId);

        // Nur ein Admin oder der Ersteller haben Zugriff
        if (!securityIdentity.hasRole("admin") && !bet.getProfile().getId().equals(profileId)) {
            throw new OperationNotAllowedException(Bet.class.getName(), betId.toString(),
                    "only an admin or the creator has access");
        }

        if (updateDTO.goalsTeam1 != null) {
            betCatalog.updateGoalsTeam1ById(betId, updateDTO.goalsTeam1);
        }

        if (updateDTO.goalsTeam2 != null) {
            betCatalog.updateGoalsTeam2ById(betId, updateDTO.goalsTeam2);
        }

        return betCatalog.findById(betId);
    }

    @Override
    @Timed(name = "removeBetTimer", description = "A measure of how long it takes to remove one bet", unit = MetricUnits.MILLISECONDS)
    public boolean removeById(String profileId, Long betId) {
        Bet bet = this.betCatalog.findById(betId);

        // Nur ein Admin oder der Ersteller haben Zugriff
        if (!securityIdentity.hasRole("admin") && !bet.getProfile().getId().equals(profileId)) {
            throw new OperationNotAllowedException(Bet.class.getName(), betId.toString(),
                    "only an admin or the creator has access");
        }

        return betCatalog.removeById(betId);
    }

    @Override
    @Timed(name = "getBetByMatchIdTimer", description = "A measure of how long it takes to get one bet by matchId", unit = MetricUnits.MILLISECONDS)
    public Collection<Bet> findByMatchId(Long matchId) {
        return this.betCatalog.findByMatchId(matchId);
    }

    @Override
    @Timed(name = "getByProfileIdTimer", description = "A measure of how long it takes to get one bet by profileId", unit = MetricUnits.MILLISECONDS)
    public Collection<Bet> findByProfileId(String profileId) {
        return this.betCatalog.findByProfileId(profileId);
    }

    @Counted(name = "finishedMatches", description = "How many matches have been finished.")
    public void matchFinishedEventObserver(@Observes MatchFinishedDTO eventDTO) {
        if (eventDTO == null) {
            return;
        }
        Collection<Bet> betsOfFinishedMatch = findByMatchId(eventDTO.machtId);
        Collection<ProfilePointsDTO> profilePoints = new ArrayList<ProfilePointsDTO>();

        for (Bet bet : betsOfFinishedMatch) {
            Integer matchGoalsTeam1 = eventDTO.goalsTeam1;
            Integer matchGoalsTeam2 = eventDTO.goalsTeam2;
            Integer betGoalsTeam1 = bet.getGoalsTeam1();
            Integer betGoalsTeam2 = bet.getGoalsTeam2();

            // Genau richtig = 4 Punkte
            if (betGoalsTeam1 == matchGoalsTeam1 && betGoalsTeam2 == matchGoalsTeam2) {
                profilePoints.add(new ProfilePointsDTO(bet.getProfile().getId(), 4L));
                continue;
            }

            // Differenz richtig = 3 Punkte
            if (Math.abs(matchGoalsTeam1 - matchGoalsTeam2) == Math.abs(betGoalsTeam1 - betGoalsTeam2)) {
                profilePoints.add(new ProfilePointsDTO(bet.getProfile().getId(), 3L));
                continue;
            }

            // Gewinner richtig = 2 Punkte
            if (matchGoalsTeam1 > matchGoalsTeam2 && betGoalsTeam1 > betGoalsTeam2 ||
                    matchGoalsTeam1 < matchGoalsTeam2 && betGoalsTeam1 < betGoalsTeam2) {
                profilePoints.add(new ProfilePointsDTO(bet.getProfile().getId(), 2L));
                continue;
            }
        }
        increaseProfilePointsEvent.fire(new IncreaseProfilePointsDTO(profilePoints));
    }

}
