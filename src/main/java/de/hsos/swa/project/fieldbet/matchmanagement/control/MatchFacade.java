package de.hsos.swa.project.fieldbet.matchmanagement.control;

import java.util.Collection;

import de.hsos.swa.project.fieldbet.matchmanagement.entity.Match;

/**
 * MatchFacade
 * 
 * @author Julian Voss
 */
public interface MatchFacade {
    /**
     * Erstellen eines Matches
     * 
     * @param creationDTO MatchCreationDTO mit Team IDs und Startdatum
     * @return Erstelltes Match
     */
    public Match create(MatchCreationDTO creationDTO);

    /**
     * Auslesen eines Matches
     * 
     * @param matchId Match ID
     * @return Gefundenes Match
     */
    public Match findById(Long matchId);

    /**
     * Auslesen aller Matches
     * 
     * @return Liste aller Matches
     */
    public Collection<Match> findAll();

    /**
     * Auslesen aller nicht beendeten Matches
     * 
     * @return Liste aller nicht beendeten Matches
     */
    public Collection<Match> findNotFinished();

    /**
     * Aktualisieren eins Matches
     * 
     * @param matchId   Match ID
     * @param updateDTO MatchUpdateDTO mit Anzahl Toren der Teams, Startdatum,
     *                  Kennzeichnung ob Spiel beendet ist
     * @return Aktualisiertes Match
     */
    public Match updateById(Long matchId, MatchUpdateDTO updateDTO);

    /**
     * Loeschen eines Matches
     * 
     * @param matchId Match ID
     * @return Status des Loeschvorgangs
     */
    public boolean removeById(Long matchId);
}
