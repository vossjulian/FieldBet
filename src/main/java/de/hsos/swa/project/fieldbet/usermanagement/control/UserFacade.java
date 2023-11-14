package de.hsos.swa.project.fieldbet.usermanagement.control;

/**
 * UserFacade
 * 
 * @author Julian Voss
 */
public interface UserFacade {
    /**
     * Erstellen eines Nutzerkontos mit User Rechten
     * 
     * @param creationDTO UserCreationDTO mit Username, Passwort, Vor- und Nachname
     * @return ID des erstellten Nutzerkontos
     */
    public String createUser(UserCreationDTO creationDTO);

    /**
     * Erstellen eines Nutzerkontos mit Admin Rechten
     * 
     * @param creationDTO UserCreationDTO mit Username, Passwort, Vor- und Nachname
     * @return ID des erstellten Nutzerkontos
     */
    public String createAdmin(UserCreationDTO creationDTO);
}
