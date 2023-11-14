package de.hsos.swa.project.fieldbet.usermanagement.entity;

/**
 * UserCatalog {
 * 
 * @author Julian Voss
 */
public interface UserCatalog {
    /**
     * Erstellen eines Nutzerkontos mit User Rechten
     * 
     * @param username  Username
     * @param password  Passwort
     * @param firstname Vorname
     * @param lastname  Nachname
     * @return ID des erstellten Nutzerkontos
     */
    public String createUser(String username, String password, String firstname, String lastname);

    /**
     * Erstellen eines Nutzerkontos mit Admin Rechten
     * 
     * @param username  Username
     * @param password  Passwort
     * @param firstname Vorname
     * @param lastname  Nachname
     * @return ID des erstellten Nutzerkontos
     */
    public String createAdmin(String username, String password, String firstname, String lastname);

}
