package de.hsos.swa.project.fieldbet.matchmanagement.control;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import de.hsos.swa.project.fieldbet.matchmanagement.entity.Match;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.MatchCatalog;
import de.hsos.swa.project.fieldbet.shared.control.MatchFinishedDTO;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityCreationException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityDeleteException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityQueryException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityReadException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityUpdateException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.ReferenceNotFoundException;

/**
 * Match Manager
 * 
 * @author Patrick Felschen, Julian Voss
 */
@ApplicationScoped
@Retry(maxRetries = 4)
@Timeout(250)
@CircuitBreaker(requestVolumeThreshold = 4, skipOn = {
        EntityCreationException.class,
        EntityDeleteException.class,
        EntityQueryException.class,
        EntityReadException.class,
        EntityUpdateException.class,
        ReferenceNotFoundException.class,
})
public class MatchManager implements MatchFacade {

    private MatchCatalog matchCatalog;

    @Inject
    Event<MatchFinishedDTO> matchFinishedEvent;

    @Inject
    public MatchManager(MatchCatalog matchCatalog) {
        this.matchCatalog = matchCatalog;
    }

    @Override
    @Counted(name = "createdMatches", description = "How many matches have been created.")
    @Timed(name = "createdMatchesTimer", description = "A measure of how long it takes create a match", unit = MetricUnits.MILLISECONDS)
    public Match create(MatchCreationDTO creationDTO) {
        Match createdMatch = this.matchCatalog.create(
                creationDTO.team1Id,
                creationDTO.team2Id,
                0, 0,
                creationDTO.startDateTime,
                false);
        return createdMatch;
    }

    @Override
    @Timed(name = "findMatchTimer", description = "A measure of how long it takes to get one match", unit = MetricUnits.MILLISECONDS)
    public Match findById(Long matchId) {
        return this.matchCatalog.findById(matchId);
    }

    @Override
    @Timed(name = "findAllMatchesTimer", description = "A measure of how long it takes to get all matches", unit = MetricUnits.MILLISECONDS)
    public Collection<Match> findAll() {
        return this.matchCatalog.findAll();
    }

    @Override
    @Timed(name = "findNotFinished", description = "A measure of how long it takes to get all matches", unit = MetricUnits.MILLISECONDS)
    public Collection<Match> findNotFinished() {
        return this.matchCatalog.findNotFinished();
    }

    @Override
    @Timed(name = "updateMatchTimer", description = "A measure of how long it takes to update one match", unit = MetricUnits.MILLISECONDS)
    public Match updateById(Long matchId, MatchUpdateDTO updateDTO) {
        if (updateDTO.goalsTeam1 != null) {
            this.matchCatalog.updateGoalsTeam1ById(matchId, updateDTO.goalsTeam1);
        }

        if (updateDTO.goalsTeam2 != null) {
            this.matchCatalog.updateGoalsTeam2ById(matchId, updateDTO.goalsTeam2);
        }

        if (updateDTO.isFinished != null) {
            Match match = this.matchCatalog.findById(matchId);
            // Match wurde beendet
            if (updateDTO.isFinished == true && match.isFinished() == false) {
                this.matchCatalog.updateIsFinishedById(matchId, updateDTO.isFinished);
                this.matchFinishedEvent
                        .fire(new MatchFinishedDTO(
                                match.getId(),
                                match.getGoalsTeam1(),
                                match.getGoalsTeam2()));
            }
        }

        if (updateDTO.startDateTime != null) {
            this.matchCatalog.updateDateTimeById(matchId, updateDTO.startDateTime);
        }

        return this.matchCatalog.findById(matchId);
    }

    @Override
    @Timed(name = "removeMatchTimer", description = "A measure of how long it takes to remove one match", unit = MetricUnits.MILLISECONDS)
    public boolean removeById(Long matchId) {
        return this.matchCatalog.removeById(matchId);
    }

}
