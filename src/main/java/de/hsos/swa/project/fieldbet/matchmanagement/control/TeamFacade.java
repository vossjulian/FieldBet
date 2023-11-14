package de.hsos.swa.project.fieldbet.matchmanagement.control;

import java.util.Collection;

import de.hsos.swa.project.fieldbet.matchmanagement.entity.Team;

/**
 * TeamFacade
 * 
 * @author Julian Voss
 */
public interface TeamFacade {
    /**
     * Erstellen eines Teams
     * 
     * @param creationDTO TeamCreationDTO mit Name und Alias
     * @return Erstelltes Team
     */
    public Team create(TeamCreationDTO creationDTO);

    /**
     * Auslesen eines Teams
     * 
     * @param teamId Team ID
     * @return Gefundenes Team
     */
    public Team findById(Long teamId);

    /**
     * Auslesesn aller Teams
     * 
     * @return Liste alles Teams
     */
    public Collection<Team> findAll();

    /**
     * Aktualisieren eines Teams
     * 
     * @param teamId    Team ID
     * @param updateDTO TeamUpdateDTO mit Name und Alias
     * @return Aktualisiertes Team
     */
    public Team updateById(Long teamId, TeamUpdateDTO updateDTO);

    /**
     * Loeschen eines Teams
     * 
     * @param teamId Team ID
     * @return Status des Loeschvorgangs
     */
    public boolean removeById(Long teamId);
}
