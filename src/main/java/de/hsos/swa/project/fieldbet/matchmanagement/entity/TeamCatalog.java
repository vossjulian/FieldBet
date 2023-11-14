package de.hsos.swa.project.fieldbet.matchmanagement.entity;

import java.util.Collection;

/**
 * Team Catalog
 *
 * @author Patrick Felschen
 */
public interface TeamCatalog {
    /**
     * Erstellen eines Teams
     * 
     * @param name  Name des Teams
     * @param alias Alias des Teams
     * @return Erstelltes Team
     */
    public Team create(String name, String alias);

    /**
     * Auslesens eines Teams
     * 
     * @param teamId Team ID
     * @return Ausgelesenes Team
     */
    public Team findById(Long teamId);

    /**
     * Auslesen aller Teams
     * 
     * @return Liste aller Teams
     */
    public Collection<Team> findAll();

    /**
     * Aktualisieren des Namens eines Teams
     * 
     * @param teamId Team ID
     * @param name   Name des Teams
     * @return Status des Aktualisierens
     */
    public boolean updateNameById(Long teamId, String name);

    /**
     * Aktualisieren des Aliases eines Teams
     * 
     * @param teamId Team ID
     * @param alias  Alias des Teams
     * @return Status des Aktualisierens
     */
    public boolean updateAliasById(Long teamId, String alias);

    /**
     * Loeschen eines Teams
     * 
     * @param teamId Team ID
     * @return Status des Loeschvorgangs
     */
    public boolean removeById(Long teamId);
}
