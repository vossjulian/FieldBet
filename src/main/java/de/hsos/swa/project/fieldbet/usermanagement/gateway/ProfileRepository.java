package de.hsos.swa.project.fieldbet.usermanagement.gateway;

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

import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityCreationException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityReadException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityUpdateException;
import de.hsos.swa.project.fieldbet.usermanagement.entity.Profile;
import de.hsos.swa.project.fieldbet.usermanagement.entity.ProfileCatalog;

/**
 * ProfileRepository
 * 
 * @author Patrick Felschen
 */

@ApplicationScoped
public class ProfileRepository implements ProfileCatalog {

    private static final Logger LOG = Logger.getLogger(ProfileRepository.class);

    private EntityManager em;

    private Validator validator;

    @Inject
    public ProfileRepository(EntityManager em, Validator validator) {
        this.em = em;
        this.validator = validator;
    }

    @Transactional
    @Override
    public Profile create(String profileId, String firstname, String lastname, Long points) {
        LOG.infof(
                "ProfileRepository#create called with profileId: %s, firstname: %s, lastname: %s, points: %d",
                profileId, firstname, lastname, points);

        Profile entity = new Profile(profileId, firstname, lastname, points);

        Set<ConstraintViolation<Profile>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityCreationException(Profile.class.getName(), violations);
        }

        try {
            this.em.persist(entity);
            this.em.flush();
            return entity;
        } catch (Exception e) {
            throw new EntityCreationException(Profile.class.getName(), e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Profile findById(String profileId) {
        LOG.infof("ProfileRepository#findById called with profileId: %s", profileId);

        try {
            Profile profile = this.em.find(Profile.class, profileId);
            if (profile == null) {
                throw new EntityReadException(Profile.class.getName(), profileId, "the entity does not exist");
            }
            return profile;
        } catch (Exception e) {
            LOG.warnf("ProfileRepository#findById no profile found for profileId: %s", profileId);
            throw new EntityReadException(Profile.class.getName(), profileId, e.getLocalizedMessage());
        }
    }

    @Transactional
    @Override
    public Collection<Profile> findAll() {
        LOG.info("ProfileRepository#findAll called");
        try {
            TypedQuery<Profile> query = em.createNamedQuery("Profile.findAll", Profile.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Override
    public boolean increasePointsById(String profileId, Long points) {
        LOG.infof("ProfileRepository#increasePointsById called with profileId: %s, points: %d", profileId, points);
        Profile entity = findById(profileId);
        entity.setPoints(entity.getPoints() + points);

        Set<ConstraintViolation<Profile>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityUpdateException(Profile.class.getName(), profileId, violations);
        }

        try {
            this.em.merge(entity);
            return true;
        } catch (Exception e) {
            throw new EntityUpdateException(Profile.class.getName(), profileId, e.getLocalizedMessage());
        }
    }

}