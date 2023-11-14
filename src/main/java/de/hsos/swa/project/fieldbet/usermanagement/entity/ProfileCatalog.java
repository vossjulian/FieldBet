package de.hsos.swa.project.fieldbet.usermanagement.entity;

import java.util.Collection;

/**
 * ProfileCatalog
 * 
 * @author Patrick Felschen
 */
public interface ProfileCatalog {
    /**
     * Erstellen eines Profils
     * 
     * @param profileId Profil ID
     * @param firstname Vorname
     * @param lastname  Nachname
     * @param points    Anzahl Punkte
     * @return Erstelltes Profil
     */
    public Profile create(String profileId, String firstname, String lastname, Long points);

    /**
     * Auslesen eines Profils
     * 
     * @param profileId Profil ID
     * @return Ausgelesenes Profil
     */
    public Profile findById(String profileId);

    /**
     * Auslesen aller Profile
     * 
     * @return Liste aller Profile
     */
    public Collection<Profile> findAll();

    /**
     * Aktualisieren der Punkteanzahl eines Profils
     * 
     * @param profileId   Profil ID
     * @param pointsToAdd Zu addierende Punktzahl
     * @return Status des Aktualisierens
     */
    public boolean increasePointsById(String profileId, Long pointsToAdd);
}
