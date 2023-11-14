package de.hsos.swa.project.fieldbet.matchmanagement.gateway;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.logging.Logger;

import de.hsos.swa.project.fieldbet.matchmanagement.entity.Team;
import de.hsos.swa.project.fieldbet.matchmanagement.entity.TeamCatalog;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityCreationException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityDeleteException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityQueryException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityReadException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityUpdateException;

/**
 * Team Repository
 * 
 * @author Patrick Felschen
 */
@ApplicationScoped
public class TeamRepository implements TeamCatalog {

    private static final Logger LOG = Logger.getLogger(TeamRepository.class);

    private EntityManager em;

    private Validator validator;

    @Inject
    public TeamRepository(EntityManager em, Validator validator) {
        this.em = em;
        this.validator = validator;
    }

    @Transactional
    @Override
    public Team create(String name, String alias) {
        LOG.infof("TeamRepository#create called with name: %s, alias: %s", name, alias);

        Team entity = new Team(name, alias);

        Set<ConstraintViolation<Team>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityCreationException(Team.class.getName(), violations);
        }

        try {
            this.em.persist(entity);
            this.em.flush();
            return entity;
        } catch (Exception e) {
            throw new EntityCreationException(Team.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Team findById(Long teamId) {
        LOG.infof("TeamRepository#findById called with teamId: %d", teamId);

        try {
            Team team = this.em.find(Team.class, teamId);
            if (team == null) {
                throw new EntityReadException(Team.class.getName(), teamId.toString(), "the entity does not exist");
            }
            return team;
        } catch (Exception e) {
            LOG.warnf("TeamRepository#findById no team found for teamId: %d", teamId);
            throw new EntityReadException(Team.class.getName(), teamId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Collection<Team> findAll() {
        LOG.info("TeamRepository#findAll called");
        try {
            TypedQuery<Team> query = em.createNamedQuery("Team.findAll", Team.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new EntityQueryException(Team.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateNameById(Long teamId, String name) {
        LOG.infof("TeamRepository#updateNameById called with teamId: %d, name: %s", teamId, name);
        Team entity = findById(teamId);
        entity.setName(name);

        Set<ConstraintViolation<Team>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityUpdateException(Team.class.getName(), teamId.toString(), violations);
        }

        try {
            this.em.merge(entity);
            return true;
        } catch (Exception e) {
            throw new EntityUpdateException(Team.class.getName(), teamId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean updateAliasById(Long teamId, String alias) {
        LOG.infof("TeamRepository#updateAliasById called with teamId: %d, alias: %s", teamId, alias);
        Team entity = findById(teamId);
        entity.setAlias(alias);

        Set<ConstraintViolation<Team>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityUpdateException(Team.class.getName(), teamId.toString(), violations);
        }

        try {
            this.em.merge(entity);
            return true;
        } catch (Exception e) {
            throw new EntityUpdateException(Team.class.getName(), teamId.toString(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public boolean removeById(Long teamId) {
        LOG.infof("TeamRepository#removeById called with teamId: %d", teamId);

        Team entity = findById(teamId);

        String references = entity.getMatches()
                .stream()
                .map(match -> match.getId().toString())
                .collect(Collectors.joining(", "));

        if (!entity.getMatches().isEmpty()) {
            throw new EntityDeleteException(Team.class.getName(), teamId.toString(),
                    "could not remove team because of existing references to matches: " + references);
        }

        try {
            this.em.remove(entity);
            this.em.flush();
            return true;
        } catch (Exception e) {
            LOG.warnf("MatchRepository#removeById could not remove teamId: %s", teamId);
            throw new EntityDeleteException(Team.class.getName(), teamId.toString(),
                    e.getLocalizedMessage());
        }
    }

}
