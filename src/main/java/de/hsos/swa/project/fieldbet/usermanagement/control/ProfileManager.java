package de.hsos.swa.project.fieldbet.usermanagement.control;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import de.hsos.swa.project.fieldbet.shared.control.IncreaseProfilePointsDTO;
import de.hsos.swa.project.fieldbet.shared.control.ProfilePointsDTO;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityCreationException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityDeleteException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityQueryException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityReadException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.EntityUpdateException;
import de.hsos.swa.project.fieldbet.shared.gateway.exceptions.ReferenceNotFoundException;
import de.hsos.swa.project.fieldbet.usermanagement.entity.Profile;
import de.hsos.swa.project.fieldbet.usermanagement.entity.ProfileCatalog;

/**
 * ProfileManager
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
public class ProfileManager implements ProfileFacade {

    private ProfileCatalog profileCatalog;

    @Inject
    public ProfileManager(ProfileCatalog profileCatalog) {
        this.profileCatalog = profileCatalog;
    }

    @Override
    @Counted(name = "createdProfiles", description = "How many profiles have been created.")
    @Timed(name = "createdProfilesTimer", description = "A measure of how long it takes create a profile", unit = MetricUnits.MILLISECONDS)
    public Profile create(String profileId, ProfileCreationDTO creationDTO) {
        return this.profileCatalog.create(profileId, creationDTO.firstname, creationDTO.lastname, 0L);
    }

    @Override
    @Timed(name = "findProfileByProfileIdTimer", description = "A measure of how long it takes to get one profile by profileId", unit = MetricUnits.MILLISECONDS)
    public Profile findById(String profileId) {
        return this.profileCatalog.findById(profileId);
    }

    @Override
    @Timed(name = "findAllProfileTimer", description = "A measure of how long it takes to get all profiles", unit = MetricUnits.MILLISECONDS)
    public Collection<Profile> findAll() {
        return this.profileCatalog.findAll();
    }

    @Override
    @Timed(name = "updateProfileTimer", description = "A measure of how long it takes to update one profile", unit = MetricUnits.MILLISECONDS)
    public Profile updateById(String profileId, ProfileUpdateDTO updateDTO) {
        if (updateDTO.points != null) {
            this.profileCatalog.increasePointsById(profileId, updateDTO.points);
        }
        return this.profileCatalog.findById(profileId);
    }

    public void increaseProfilePointsEventObserver(@Observes IncreaseProfilePointsDTO eventDTO) {
        for (ProfilePointsDTO profilePointsDTO : eventDTO.profilePoints) {
            ProfileUpdateDTO updateDTO = new ProfileUpdateDTO(profilePointsDTO.points);
            this.updateById(profilePointsDTO.profileId, updateDTO);
        }
    }

}
