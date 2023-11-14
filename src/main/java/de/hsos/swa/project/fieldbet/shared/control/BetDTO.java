package de.hsos.swa.project.fieldbet.shared.control;

import java.util.Collection;
import java.util.stream.Collectors;

import de.hsos.swa.project.fieldbet.betmanagement.entity.Bet;

/**
 * BetDTO
 * 
 * @author Patrick Felschen
 */
public class BetDTO {
    public Long id;
    public MatchDTO match;
    public ProfileDTO profile;
    public Integer goalsTeam1;
    public Integer goalsTeam2;

    public BetDTO() {
    }

    public BetDTO(Long id, MatchDTO match, ProfileDTO profile, Integer goalsTeam1, Integer goalsTeam2) {
        this.id = id;
        this.match = match;
        this.profile = profile;
        this.goalsTeam1 = goalsTeam1;
        this.goalsTeam2 = goalsTeam2;
    }

    public static class Converter {
        public static BetDTO toDTO(Bet bet) {
            if (bet == null)
                return null;
            return new BetDTO(
                    bet.getId(),
                    MatchDTO.Converter.toDTO(bet.getMatch()),
                    ProfileDTO.Converter.toDTO(bet.getProfile()),
                    bet.getGoalsTeam1(),
                    bet.getGoalsTeam2());
        }

        public static Collection<BetDTO> toDTOList(Collection<Bet> bets) {
            return bets.stream().map(BetDTO.Converter::toDTO).collect(Collectors.toList());
        }
    }
}
