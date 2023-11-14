package de.hsos.swa.project.fieldbet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import de.hsos.swa.project.fieldbet.usermanagement.control.UserCreationDTO;
import de.hsos.swa.project.fieldbet.usermanagement.control.UserFacade;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;

/**
 * Startup
 * 
 * @author Julian Voss
 */
@ApplicationScoped
public class Startup {

    private UserFacade userFacade;

    @Inject
    public Startup(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public void init(@Observes StartupEvent event) {
        if (LaunchMode.current() != LaunchMode.DEVELOPMENT) {
            return;
        }

        this.userFacade.createAdmin(new UserCreationDTO("juliavos", "juliavos", "Julian", "Voss"));
        this.userFacade.createAdmin(new UserCreationDTO("pfelsche", "pfelsche", "Patrick", "Felschen"));
        this.userFacade.createUser(new UserCreationDTO("user", "user", "Max", "Mustermann"));
    }
}
