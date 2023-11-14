package de.hsos.swa.project.fieldbet.betmanagement.control;

import java.util.Collection;

import de.hsos.swa.project.fieldbet.betmanagement.entity.Bet;

/**
 * BetFacade
 * 
 * @author Patrick Felschen
 */
public interface BetFacade {

    /**
     * Erstellt eine Wette fuer ein angegebenes Profil an
     * 
     * @param profileId   Angabe der ID des zugehoerigen Profils
     * @param creationDTO Angaben der Informationen, welche benoetigt werden um eine
     *                    Wette zu erstellen
     * @return erstellte Wetten Entity
     */
    public Bet create(String profileId, BetCreationDTO creationDTO);

    /**
     * Gibt eine Wette aus, welche eine bestimmte ID hat
     * 
     * @param profileId ID des aufrufenden Profils
     * @param betId     ID der Wette nach der gesucht werden soll
     * @return gefundene Entity der Wette
     */
    public Bet findById(String profileId, Long betId);

    /**
     * Gibt alle Wetten aus, welche zu einem bestimmten Spiel gehoeren
     * 
     * @param matchId ID des Spiels
     * @return Liste aller Wetten, welche zum Spiel gehoeren
     */
    public Collection<Bet> findByMatchId(Long matchId);

    /**
     * Gibt alle Wetten aus, welche zu einem bestimmten Profil gehoeren
     * 
     * @param profileId ID des Profils
     * @return Liste aller Wetten, welche zum Profil gehoeren
     */
    public Collection<Bet> findByProfileId(String profileId);

    /**
     * Gibt alle Wetten aus, die existieren
     * 
     * @return Liste aller Wetten
     */
    public Collection<Bet> findAll();

    /**
     * Gibt alle Wetten eines Profils aus, welche noch nicht beendet wurden.
     * 
     * @param profileId ID des Profils
     * @return Liste der gefundenen Wetten
     */
    public Collection<Bet> findNotFinishedByProfileId(String profileId);

    /**
     * Aktualisiert eine bestimmt Wette
     * 
     * @param profileId ID des aufrufenden Profils
     * @param betId     ID der Wette, welche aktualisiert werden soll
     * @param updateDTO vorzunehmende Aktualisierungen der Wette
     * @return aktualisierte Wette
     */
    public Bet updateById(String profileId, Long betId, BetUpdateDTO updateDTO);

    /**
     * Entfernt eine Wette dauerhaft
     * 
     * @param profileId ID des aufrufenden Profils
     * @param betId     ID der Wette, welche entfernt werden soll
     * @return true, wenn die Wette erfolgreich entfernt wurde
     */
    public boolean removeById(String profileId, Long betId);
}
