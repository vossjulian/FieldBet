package de.hsos.swa.project.fieldbet.usermanagement.gateway;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import de.hsos.swa.project.fieldbet.usermanagement.entity.UserCatalog;

/**
 * UserRepository
 * 
 * @author Julian Voss
 */
@ApplicationScoped
public class UserRepository implements UserCatalog {

    private static final Logger LOG = Logger.getLogger(UserRepository.class);

    private Keycloak keycloak;
    private RealmResource realmResource;
    private UsersResource usersResource;

    public UserRepository() {
    }

    /**
     * Initialisert den Keycloak Admin Client fuer den Master Realm
     */
    @PostConstruct
    public void initKeycloak() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180")
                .realm("master")
                .clientId("admin-cli")
                .grantType("password")
                .username("admin")
                .password("admin")
                .build();
        this.realmResource = keycloak.realm("quarkus");
        this.usersResource = realmResource.users();
    }

    @Override
    public String createUser(String username, String password, String firstname, String lastname) {
        RoleRepresentation userRole = realmResource.roles().get("user").toRepresentation();

        return this.create(username, password, firstname, lastname, List.of(userRole));
    }

    @Override
    public String createAdmin(String username, String password, String firstname, String lastname) {
        RoleRepresentation userRole = realmResource.roles().get("user").toRepresentation();
        RoleRepresentation adminRole = realmResource.roles().get("admin").toRepresentation();

        return this.create(username, password, firstname, lastname, List.of(userRole, adminRole));
    }

    /**
     * Erstellen eines Keycloak Nutzerkontos
     * https://gist.github.com/thomasdarimont/c4e739c5a319cf78a4cff3b87173a84b
     * 
     * @param username  Username
     * @param password  Passwort
     * @param firstname Vorname
     * @param lastname  Nachname
     * @param roles     Rollen des Nutzers
     * @return ID des erstellten Nutzerkontos
     */
    private String create(String username, String password, String firstname, String lastname,
            List<RoleRepresentation> roles) {
        List<UserRepresentation> users = usersResource.searchByUsername(username, true);
        if (!users.isEmpty()) {
            String userid = users.get(0).getId();
            LOG.infof("User exists id: %s", userid);
            return userid;
        }

        // Nutzerdaten
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);

        Response response = usersResource.create(user);
        LOG.infof("Response: %s %s%n", response.getStatus(), response.getStatusInfo());
        LOG.info(response.getLocation());

        String userId = CreatedResponseUtil.getCreatedId(response);

        LOG.infof("User created with userId: %s%n", userId);

        // Passwort
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);

        UserResource userResource = usersResource.get(userId);
        userResource.resetPassword(passwordCred);

        // Rollen
        userResource.roles().realmLevel().add(roles);

        return userId;
    }

}
