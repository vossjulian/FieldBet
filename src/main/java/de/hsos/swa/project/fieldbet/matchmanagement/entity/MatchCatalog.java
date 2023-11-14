package de.hsos.swa.project.fieldbet.matchmanagement.entity;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * MatchCatalog
 * 
 * @author Patrick Felschen
 */
public interface MatchCatalog {
    /**
     * Erstellen eines Matches
     * 
     * @param team1Id       ID Team 1
     * @param team2Id       ID Team 2
     * @param goalsTeam1    Anzahl Tore Team 1
     * @param goalsTeam2    Anzahl Tore Team 2
     * @param startDateTime Startdatum
     * @param isFinished    Kennzeichnung ob Matche beendet wurde
     * @return Erstelltes Match
     */
    public Match create(Long team1Id, Long team2Id, Integer goalsTeam1, Integer goalsTeam2, LocalDateTime startDateTime,
            boolean isFinished);

    /**
     * Auslesen eines Matches
     * 
     * @param matchId Match ID
     * @return Ausgelesenes Match
     */
    public Match findById(Long matchId);

    /**
     * Auslesen aller Matches
     * 
     * @return Ausgelesenes Match
     */
    public Collection<Match> findAll();

    /**
     * Auslesen aller Matches
     * 
     * @return Liste aller Matches
     */
    public Collection<Match> findNotFinished();

    /**
     * Aktualisieren der Tore des ersten Teams
     * 
     * @param matchId Match ID
     * @param goals   Anzahl Tore
     * @return Status des Aktualisierens
     */
    public boolean updateGoalsTeam1ById(Long matchId, Integer goals);

    /**
     * Aktualisieren der Tore des zweiten Teams
     * 
     * @param matchId Match ID
     * @param goals   Anzahl Tore
     * @return Status des Aktualisierens
     */
    public boolean updateGoalsTeam2ById(Long matchId, Integer goals);

    /**
     * Aktualisiert isFinished des Matches
     * 
     * @param matchId    Match ID
     * @param isFinished Spiel beendet
     * @return Status des Aktualisierens
     */
    public boolean updateIsFinishedById(Long matchId, boolean isFinished);

    /**
     * Aktualisiert das Startdatum des Matches
     * 
     * @param matchId  Match ID
     * @param dateTime Startdatum
     * @return Status des Aktualisierens
     */
    public boolean updateDateTimeById(Long matchId, LocalDateTime dateTime);

    /**
     * Loeschen eines Matches
     * 
     * @param matchId Match ID
     * @return Status des Loeschvorgangs
     */
    public boolean removeById(Long matchId);

}
