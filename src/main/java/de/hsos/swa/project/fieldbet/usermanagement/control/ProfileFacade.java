package de.hsos.swa.project.fieldbet.usermanagement.control;

import java.util.Collection;

import de.hsos.swa.project.fieldbet.usermanagement.entity.Profile;

/**
 * ProfileFacade
 * 
 * @author Patrick Felschen
 */
public interface ProfileFacade {
    /**
     * Erstellen eines Profil
     * 
     * @param profileId   Profil ID
     * @param creationDTO ProfileCreationDTO mit Vor- und Nachname
     * @return Erstelltes Profil
     */
    public Profile create(String profileId, ProfileCreationDTO creationDTO);

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
     * @return Liste alles Profile
     */
    public Collection<Profile> findAll();

    /**
     * Aktualisieren eines Profils
     * 
     * @param profileId Profil ID
     * @param updateDTO ProfilUpdateDTO mit Anzahl Punkten
     * @return Aktualisiertes Profil
     */
    public Profile updateById(String profileId, ProfileUpdateDTO updateDTO);
}
