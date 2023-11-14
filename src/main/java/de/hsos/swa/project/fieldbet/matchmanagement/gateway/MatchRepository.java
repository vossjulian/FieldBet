package de.hsos.swa.project.fieldbet.matchmanagement.gateway;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.logging.Logger;

import de.hsos.swa.project.fieldbet.matchmanagement.entity.Match;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.MatchCatalog;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.Team;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityCreationException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityDeleteException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityQueryException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityReadException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityUpdateException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.ReferenceNotFoundException;

/**
 * Match Repository
 * 
 * @author Patrick Felschen
 */
@ApplicationScoped
public class MatchRepository implements MatchCatalog {

    private static final Logger LOG = Logger.getLogger(MatchRepository.class);

    private EntityManager em;

    private Validator validator;

    @Inject
    public MatchRepository(EntityManager em, Validator validator) {
        this.em = em;
        this.validator = validator;
    }

    /**
     * Sucht nach einer Team-Referenz und gibt diese zurueck
     * 
     * @param teamId ID des Teams
     * @return gefundene Team-Referenz
     */
    @Transactional
    private Team findTeamReference(Long teamId) {
        LOG.infof("MatchRepository#findTeamReference called with teamId: %s", teamId);
        if (teamId == null) {
            throw new ReferenceNotFoundException(Team.class.getName(), "", "id to load is required for loading");
        }
        try {
            Team team = this.em.getReference(Team.class, teamId);
            LOG.infof("MatchRepository#findTeamReference found team: %s", team.toString());
            return team;
        } catch (Exception e) {
            LOG.warnf("MatchRepository#findTeamReference no team found for teamId:", teamId);
            throw new ReferenceNotFoundException(Team.class.getName(), teamId.toString(),
                    e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Match create(Long team1Id, Long team2Id, Integer goalsTeam1, Integer goalsTeam2, LocalDateTime startDateTime,
            boolean isFinished) {
        LOG.infof(
                "MatchRepository#create called with team1Id: %d, team2Id: %d, goalsTeam1: %d, goalsTeam2: %d, startDateTime: %s",
                team1Id, team2Id, goalsTeam1, goalsTeam2, startDateTime);

        Team team1 = findTeamReference(team1Id);
        Team team2 = findTeamReference(team2Id);

        Match entity = new Match(team1, team2, goalsTeam1, goalsTeam2, startDateTime, isFinished);

        Set<ConstraintViolation<Match>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            throw new EntityCreationException(Match.class.getName(), violations);
        }

        try {
            this.em.persist(entity);
            this.em.flush();
            return entity;
        } catch (Exception e) {
            throw new EntityCreationException(Match.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Match findById(Long matchId) {
        LOG.infof("MatchRepository#findById called with matchId: %d", matchId);
        try {
            Match match = this.em.find(Match.class, matchId);
            if (match == null) {
                throw new EntityReadException(Match.class.getName(), matchId.toString(), "the entity does not exist");
            }
            return this.em.find(Match.class, matchId);
        } catch (Exception e) {
            LOG.warnf("MatchRepository#findById no match found for matchId: %d", matchId);
            throw new EntityReadException(Match.class.getName(), matchId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Collection<Match> findAll() {
        LOG.info("MatchRepository#findAll called");
        try {
            TypedQuery<Match> query = em.createNamedQuery("Match.findAll", Match.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new EntityQueryException(Match.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Collection<Match> findNotFinished() {
        LOG.info("MatchRepository#findNotFinished called");
        try {
            TypedQuery<Match> query = em.createNamedQuery("Match.findNotFinished", Match.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new EntityQueryException(Match.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateGoalsTeam1ById(Long matchId, Integer goals) {
        LOG.infof("MatchRepository#updateGoalsTeam1ById called with matchId: %d, goals: %d", matchId, goals);
        Match entity = findById(matchId);
        entity.setGoalsTeam1(goals);

        Set<ConstraintViolation<Match>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityUpdateException(Match.class.getName(), matchId.toString(), violations);
        }

        try {
            this.em.merge(entity);
            return true;
        } catch (Exception e) {
            throw new EntityUpdateException(Match.class.getName(), matchId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateGoalsTeam2ById(Long matchId, Integer goals) {
        LOG.infof("MatchRepository#updateGoalsTeam2ById called with matchId: %d, goals: %d", matchId, goals);
        Match entity = findById(matchId);
        entity.setGoalsTeam2(goals);

        Set<ConstraintViolation<Match>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityUpdateException(Match.class.getName(), matchId.toString(), violations);
        }

        try {
            this.em.merge(entity);
            return true;
        } catch (Exception e) {
            throw new EntityUpdateException(Match.class.getName(), matchId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateIsFinishedById(Long matchId, boolean isFinished) {
        LOG.infof("MatchRepository#updateIsFinishedById called with matchId: %d, isFinished: %b", matchId, isFinished);
        Match entity = findById(matchId);
        entity.setFinished(isFinished);

        Set<ConstraintViolation<Match>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityUpdateException(Match.class.getName(), matchId.toString(), violations);
        }

        try {
            this.em.merge(entity);
            return true;
        } catch (Exception e) {
            throw new EntityUpdateException(Match.class.getName(), matchId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateDateTimeById(Long matchId, LocalDateTime startDateTime) {
        LOG.infof("MatchRepository#updateDateTimeById called with matchId: %d, startDateTime: %s", matchId,
                startDateTime);
        Match entity = findById(matchId);
        entity.setStartDateTime(startDateTime);

        Set<ConstraintViolation<Match>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityUpdateException(Match.class.getName(), matchId.toString(), violations);
        }

        try {
            this.em.merge(entity);
            return true;
        } catch (Exception e) {
            throw new EntityUpdateException(Match.class.getName(), matchId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean removeById(Long matchId) {
        LOG.infof("MatchRepository#removeById called with matchId: %s", matchId);
        Match match = findById(matchId);
        try {
            this.em.remove(match);
            this.em.flush();
            return true;
        } catch (Exception e) {
            LOG.warnf("MatchRepository#removeById could not remove matchId: %s", matchId);
            throw new EntityDeleteException(Match.class.getName(), matchId.toString(),
                    e.getLocalizedMessage());
        }
    }

}