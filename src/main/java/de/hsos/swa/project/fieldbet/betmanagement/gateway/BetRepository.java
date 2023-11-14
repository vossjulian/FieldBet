package de.hsos.swa.project.fieldbet.betmanagement.gateway;

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

import de.hsos.swa.project.fieldbet.betmanagement.entity.Bet;
import de.hsos.swa.project.fieldbet.betmanagement.entity.BetCatalog;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.Match;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityCreationException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityDeleteException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityQueryException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityReadException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityUpdateException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.ReferenceNotFoundException;
import de.hsos.swa.project.fieldbet.usermanagement.entity.Profile;

/**
 * BetRepository
 * 
 * @author Patrick Felschen
 */
@ApplicationScoped
public class BetRepository implements BetCatalog {

    private static final Logger LOG = Logger.getLogger(BetRepository.class);

    private EntityManager em;

    private Validator validator;

    @Inject
    public BetRepository(EntityManager em, Validator validator) {
        this.em = em;
        this.validator = validator;
    }

    /**
     * Sucht nach einer Spiel-Referenz und gibt diese zurueck
     * 
     * @param matchId ID des Spiels
     * @return gefundene Spiel-Referenz
     */
    @Transactional
    private Match findMatchReference(Long matchId) {
        LOG.infof("BetRepository#findMatchReference called with matchId: %s", matchId);
        if (matchId == null) {
            throw new ReferenceNotFoundException(Match.class.getName(), "", "id to load is required for loading");
        }
        try {
            Match match = this.em.getReference(Match.class, matchId);
            LOG.infof("BetRepository#findMatchReference found match: %s", match.toString());
            return match;
        } catch (Exception e) {
            LOG.warnf("BetRepository#findMatchReference no match found for matchId:", matchId);
            throw new ReferenceNotFoundException(Match.class.getName(), matchId.toString(),
                    e.getLocalizedMessage());
        }
    }

    /**
     * Sucht nach einer Profil-Referenz und gibt diese zurueck
     * 
     * @param profileId ID Des Profils
     * @return gefundene Profil-Referenz
     */
    @Transactional
    private Profile findProfileReference(String profileId) {
        LOG.infof("BetRepository#findProfileReference called with profileId: %s", profileId);
        if (profileId == null) {
            throw new ReferenceNotFoundException(Profile.class.getName(), "", "id to load is required for loading");
        }
        try {
            Profile profile = this.em.getReference(Profile.class, profileId);
            LOG.infof("BetRepository#findProfileReference found profile: %s", profile.toString());
            return profile;
        } catch (Exception e) {
            LOG.warnf("BetRepository#findProfileReference no profile found for profileId: %s", profileId);
            throw new ReferenceNotFoundException(Profile.class.getName(), profileId.toString(),
                    e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Bet create(Long matchId, String profileId, Integer goalsTeam1, Integer goalsTeam2) {
        LOG.infof("BetRepository#create called with matchId: %s, profileId: %s, goalsTeam1: %d, goalsTeam2: %d",
                matchId, profileId, goalsTeam1, goalsTeam2);

        Match match = findMatchReference(matchId);
        Profile profile = findProfileReference(profileId);

        Bet entity = new Bet(match, profile, goalsTeam1, goalsTeam2, LocalDateTime.now(), LocalDateTime.now());

        Set<ConstraintViolation<Bet>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityCreationException(Bet.class.getName(), violations);
        }

        try {
            this.em.persist(entity);
            this.em.flush();
            return entity;
        } catch (Exception e) {
            throw new EntityCreationException(Bet.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Bet findById(Long betId) {
        LOG.infof("BetRepository#findById called with betId: %s", betId);
        try {
            Bet bet = this.em.find(Bet.class, betId);
            if (bet == null) {
                throw new EntityReadException(Bet.class.getName(), betId.toString(), "the entity does not exist");
            }
            return bet;
        } catch (Exception e) {
            LOG.warnf("BetRepository#findById no bet found for betId: %s", betId);
            throw new EntityReadException(Bet.class.getName(), betId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Collection<Bet> findAll() {
        LOG.info("BetRepository#findAll called");
        try {
            TypedQuery<Bet> query = em.createNamedQuery("Bet.findAll", Bet.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new EntityQueryException(Bet.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Collection<Bet> findNotFinishedByProfileId(String profileId) {
        LOG.info("BetRepository#findNotFinished called");
        try {
            TypedQuery<Bet> query = em.createNamedQuery("Bet.findNotFinishedByProfileId", Bet.class);
            query.setParameter(1, profileId);
            return query.getResultList();
        } catch (Exception e) {
            throw new EntityQueryException(Bet.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Collection<Bet> findByMatchId(Long matchId) {
        LOG.infof("BetRepository#findByMatchId called with matchId: %s", matchId);
        try {
            TypedQuery<Bet> query = em.createNamedQuery("Bet.findByMatchId", Bet.class);
            query.setParameter(1, matchId);
            return query.getResultList();
        } catch (Exception e) {
            LOG.warnf("BetRepository#findByMatchId no bets found for matchId: %s", matchId);
            throw new EntityQueryException(Bet.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Collection<Bet> findByProfileId(String profileId) {
        LOG.infof("BetRepository#findByProfileId called with profileId: %s", profileId);
        try {
            TypedQuery<Bet> query = em.createNamedQuery("Bet.findByProfileId", Bet.class);
            query.setParameter(1, profileId);
            return query.getResultList();
        } catch (Exception e) {
            LOG.warnf("BetRepository#findByProfileId no bets found for profileId: %s", profileId);
            throw new EntityQueryException(Bet.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateGoalsTeam1ById(Long betId, Integer goalsTeam1) {
        LOG.infof("BetRepository#updateGoalsTeam1ById called with betId: %s, goalsTeam1: %s", betId, goalsTeam1);
        Bet entity = findById(betId);
        entity.setGoalsTeam1(goalsTeam1);
        entity.setModificationDateTime(LocalDateTime.now());

        Set<ConstraintViolation<Bet>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityUpdateException(Bet.class.getName(), betId.toString(), violations);
        }

        try {
            this.em.merge(entity);
            return true;
        } catch (Exception e) {
            throw new EntityUpdateException(Bet.class.getName(), betId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateGoalsTeam2ById(Long betId, Integer goalsTeam2) {
        LOG.infof("BetRepository#updateGoalsTeam2ById called with betId: %s, goalsTeam2: %s", betId, goalsTeam2);
        Bet entity = findById(betId);
        entity.setGoalsTeam2(goalsTeam2);
        entity.setModificationDateTime(LocalDateTime.now());

        Set<ConstraintViolation<Bet>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityUpdateException(Bet.class.getName(), betId.toString(), violations);
        }

        try {
            this.em.merge(entity);
            return true;
        } catch (Exception e) {
            throw new EntityUpdateException(Bet.class.getName(), betId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean removeById(Long betId) {
        LOG.infof("BetRepository#removeById called with betId: %s", betId);

        Bet entity = findById(betId);

        try {
            this.em.remove(entity);
            this.em.flush();
            return true;
        } catch (Exception e) {
            LOG.warnf("BetRepository#removeById could not remove betId: %s", betId);
            throw new EntityDeleteException(Bet.class.getName(), betId.toString(),
                    e.getLocalizedMessage());
        }
    }

}