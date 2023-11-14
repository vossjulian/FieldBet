package de.hsos.swa.project.fieldbet.usermanagement.control;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.hsos.swa.project.fieldbet.usermanagement.entity.ProfileCatalog;
import de.hsos.swa.project.fieldbet.usermanagement.entity.UserCatalog;

/**
 * UserManager
 * 
 * @author Julian Voss
 */
@ApplicationScoped
public class UserManager implements UserFacade {

    private UserCatalog userCatalog;
    private ProfileCatalog profileCatalog;

    @Inject
    public UserManager(UserCatalog userCatalog, ProfileCatalog profileCatalog) {
        this.userCatalog = userCatalog;
        this.profileCatalog = profileCatalog;
    }

    @Override
    public String createUser(UserCreationDTO creationDTO) {
        String userId = this.userCatalog.createUser(creationDTO.username, creationDTO.password, creationDTO.firstname,
                creationDTO.lastname);
        this.profileCatalog.create(userId, creationDTO.firstname, creationDTO.lastname, 0L);

        return userId;
    }

    @Override
    public String createAdmin(UserCreationDTO creationDTO) {
        String userId = this.userCatalog.createAdmin(creationDTO.username, creationDTO.password, creationDTO.firstname,
                creationDTO.lastname);
        this.profileCatalog.create(userId, creationDTO.firstname, creationDTO.lastname, 0L);

        return userId;
    }

}
