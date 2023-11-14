package de.hsos.swa.project.fieldbet.shared.control;

import java.util.Collection;
import java.util.stream.Collectors;

import de.hsos.swa.project.fieldbet.matchmanagement.entity.Team;

/**
 * TeamDTO
 * 
 * @author Julian Voss
 */
public class TeamDTO {
    public Long id;
    public String name;
    public String alias;

    public TeamDTO() {
    }

    public TeamDTO(Long id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public static class Converter {
        public static TeamDTO toDTO(Team team) {
            if (team == null)
                return null;
            return new TeamDTO(team.getId(), team.getName(), team.getAlias());
        }

        public static Team fromDTO(TeamDTO teamDTO) {
            if (teamDTO == null)
                return null;
            return new Team(teamDTO.name, teamDTO.alias);
        }

        public static Collection<TeamDTO> toDTOList(Collection<Team> teams) {
            return teams.stream().map(TeamDTO.Converter::toDTO).collect(Collectors.toList());
        }
    }
}
