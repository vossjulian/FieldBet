package de.hsos.swa.project.fieldbet.shared.control;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import de.hsos.swa.project.fieldbet.matchmanagement.entity.Match;

/**
 * MatchDTO
 * 
 * @author Julian Voss
 */
public class MatchDTO {
    public Long id;
    public TeamDTO team1;
    public TeamDTO team2;
    public Integer goalsTeam1;
    public Integer goalsTeam2;
    public LocalDateTime startDateTime;
    public boolean isFinished;

    public MatchDTO() {
    }

    public MatchDTO(Long id, TeamDTO team1, TeamDTO team2, int goalsTeam1, int goalsTeam2, LocalDateTime startDateTime,
            boolean isFinished) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.goalsTeam1 = goalsTeam1;
        this.goalsTeam2 = goalsTeam2;
        this.startDateTime = startDateTime;
        this.isFinished = isFinished;
    }

    public static class Converter {
        public static MatchDTO toDTO(Match match) {
            if (match == null)
                return null;
            return new MatchDTO(
                    match.getId(),
                    TeamDTO.Converter.toDTO(match.getTeam1()),
                    TeamDTO.Converter.toDTO(match.getTeam2()),
                    match.getGoalsTeam1(),
                    match.getGoalsTeam2(),
                    match.getStartDateTime(),
                    match.isFinished());
        }

        public static Match fromDTO(MatchDTO matchDTO) {
            if (matchDTO == null)
                return null;
            return new Match(TeamDTO.Converter.fromDTO(matchDTO.team1), TeamDTO.Converter.fromDTO(matchDTO.team2),
                    matchDTO.goalsTeam1,
                    matchDTO.goalsTeam2,
                    matchDTO.startDateTime, matchDTO.isFinished);
        }

        public static Collection<MatchDTO> toDTOList(Collection<Match> matches) {
            return matches.stream().map(MatchDTO.Converter::toDTO).collect(Collectors.toList());
        }
    }

}
