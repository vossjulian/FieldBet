package de.hsos.swa.project.fieldbet.betmanagement.entity;

import java.util.Collection;

/**
 * BetCatalog
 * 
 * @author Patrick Felschen
 */
public interface BetCatalog {

    /**
     * Erstellt eine Wette
     * 
     * @param matchId    ID des Spiels, wuzu die Wette gehoert
     * @param profileId  ID des zugehoerigen Profils
     * @param goalsTeam1 Vorhersage der Tore des ersten Teams
     * @param goalsTeam2 Vorhersage der Tore des zweiten Teams
     * @return erstellte Entity der Wette
     */
    public Bet create(Long matchId, String profileId, Integer goalsTeam1, Integer goalsTeam2);

    /**
     * Gibt eine Wette aus, welche eine bestimmte ID hat
     * 
     * @param betId ID der Wette nach der gesucht werden soll
     * @return gefundene Entity der Wette
     */
    public Bet findById(Long betId);

    /**
     * Gibt alle Wetten aus, welche zu einem bestimmten Profil gehoeren
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
     * Gibt alle Wetten aus eines Profils aus, welche noch nicht beendet wurden
     * 
     * @param profileId ID des Profils
     * @return Liste der gefundenen Wetten
     */
    public Collection<Bet> findNotFinishedByProfileId(String profileId);

    /**
     * Aktualisiert die Tore des ersten Teams einer bestimmten Wette
     * 
     * @param betId      ID der Wette, welche aktualisiert wetden soll
     * @param goalsTeam1 Tore des ersten Teams
     * @return true, wenn die Wette erfolgreich aktualisiert wurde
     */
    public boolean updateGoalsTeam1ById(Long betId, Integer goalsTeam1);

    /**
     * Aktualisiert die Tore des ersten Teams einer bestimmten Wette
     * 
     * @param betId      ID der Wette, welche aktualisiert wetden soll
     * @param goalsTeam2 Tore des zweiten Teams
     * @return true, wenn die Wette erfolgreich aktualisiert wurde
     */
    public boolean updateGoalsTeam2ById(Long betId, Integer goalsTeam2);

    /**
     * Entfernt eine Wette dauerhaft
     * 
     * @param betId ID der Wette, welche entfernt werden soll
     * @return true, wenn die Wette erfolgreich entfernt wurde
     */
    public boolean removeById(Long betId);

}
