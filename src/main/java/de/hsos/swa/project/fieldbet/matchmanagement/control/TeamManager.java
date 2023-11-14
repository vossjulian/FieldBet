package de.hsos.swa.project.fieldbet.matchmanagement.control;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import de.hsos.swa.project.fieldbet.matchmanagement.entity.Team;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.TeamCatalog;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityCreationException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityDeleteException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityQueryException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityReadException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityUpdateException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.ReferenceNotFoundException;

/**
 * TeamManager
 * 
 * @author Julian Voss, Patrick Felschen
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
public class TeamManager implements TeamFacade {

    private TeamCatalog teamCatalog;

    @Inject
    public TeamManager(TeamCatalog teamCatalog) {
        this.teamCatalog = teamCatalog;
    }

    @Override
    @Counted(name = "createdTeams", description = "How many teams have been created.")
    @Timed(name = "createdTeamsTimer", description = "A measure of how long it takes create a team", unit = MetricUnits.MILLISECONDS)
    public Team create(TeamCreationDTO creationDTO) {
        return this.teamCatalog.create(creationDTO.name, creationDTO.alias);
    }

    @Override
    @Timed(name = "removeTeamTimer", description = "A measure of how long it takes to remove one team", unit = MetricUnits.MILLISECONDS)
    public boolean removeById(Long teamId) {
        return this.teamCatalog.removeById(teamId);
    }

    @Override
    @Timed(name = "getTeamsTimer", description = "A measure of how long it takes to get all teams", unit = MetricUnits.MILLISECONDS)
    public Collection<Team> findAll() {
        return this.teamCatalog.findAll();
    }

    @Override
    @Timed(name = "getTeamTimer", description = "A measure of how long it takes to get one team", unit = MetricUnits.MILLISECONDS)
    public Team findById(Long teamId) {
        return this.teamCatalog.findById(teamId);
    }

    @Override
    @Timed(name = "updateTeamTimer", description = "A measure of how long it takes to update one team", unit = MetricUnits.MILLISECONDS)
    public Team updateById(Long teamId, TeamUpdateDTO updateDTO) {
        if (updateDTO.name != null) {
            this.teamCatalog.updateNameById(teamId, updateDTO.name);
        }

        if (updateDTO.alias != null) {
            this.teamCatalog.updateAliasById(teamId, updateDTO.alias);
        }

        return findById(teamId);
    }

}
